package kr.valor.bal.ui.schedule

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.R
import kr.valor.bal.adapters.*
import kr.valor.bal.adapters.schedule.ScheduleAdapter
import kr.valor.bal.databinding.ScheduleFragmentBinding
import kr.valor.bal.utilities.binding.ScheduleBindingParameterCreator
import kr.valor.bal.utilities.observeInLifecycle


// TODO : Set state for rest / workout
// TODO : Different UI for each state

// TODO : Additional process needed (when workout recording is done...)
@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private val viewModel: ScheduleViewModel by viewModels()

    private lateinit var binding: ScheduleFragmentBinding

    private lateinit var scheduleAdapter: ScheduleAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ScheduleFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initBinding()
        recyclerView = binding.scheduleRecyclerView.also { it.initRecyclerview() }

        viewModel.eventsFlow
            .onEach {
                when (it) {
                    ScheduleViewModel.Event.ShowAddNewWorkoutDialog -> {
                        showWorkoutSelectionDialog()
                    }
                    ScheduleViewModel.Event.ShowTimerStopActionChoiceDialog -> {
                        showTimerResetActionChoiceDialog()
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)

        viewModel.currentWorkoutSchedule.observe(viewLifecycleOwner) {
            val items =
                it.workoutDetails.map { item ->
                    WorkoutDetailItem.Item(item)
                } + listOf(WorkoutDetailItem.Footer)
            scheduleAdapter.submitList(items)
        }
    }

    private fun ScheduleFragmentBinding.initBinding() {
        bindingCreator = ScheduleBindingParameterCreator
        viewModel = this@ScheduleFragment.viewModel
        lifecycleOwner = viewLifecycleOwner
    }

    private fun RecyclerView.initRecyclerview() {
        scheduleAdapter = ScheduleAdapter(*initializeRecyclerviewClickListeners())
            .also { adapter = it }

        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding.addWorkoutButton.hide()
                } else {
                    binding.addWorkoutButton.show()
                }
            }
        })
    }

    private fun initializeRecyclerviewClickListeners(): Array<RecyclerviewItemClickListener<*>> =
        arrayOf(
            AddWorkoutSetListener { item ->
                viewModel.onAddNewSetButtonClicked(item.workoutDetail.detailId)
            },
            RemoveWorkoutSetListener { item ->
                viewModel.onDeleteSetButtonClicked(item.workoutDetail.detailId)
            },

            DropWorkoutListener { item ->
                viewModel.onCloseButtonClicked(item.workoutDetail)
            },

            UpdateWorkoutSetListener { item ->
                findNavController()
                    .navigate(ScheduleFragmentDirections
                        .actionScheduleDestToScheduleSetDialogFragment(item.setId)
                    )
            },

            CompleteWorkoutScheduleListener {
                viewModel.onWorkoutFinishButtonClicked()
            }
        )

    private fun showWorkoutSelectionDialog(): AlertDialog {
        val title = resources.getString(R.string.add_new_workout_popup_title)
        val items = resources.getStringArray(R.array.exercise_list)

        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(title)
            .setItems(items) { _ , i: Int ->
                viewModel.onDialogItemSelected(items[i])
            }
            .show()
    }

    private fun showTimerResetActionChoiceDialog(): AlertDialog {
        val dialogTitleRes = R.string.timer_stop_action_choice_dialog_title
        val dialogMessageRes = R.string.timer_stop_action_choice_dialog_message
        val dialogPositiveBtnLabelRes = R.string.timer_stop_action_choice_dialog_positive_action_btn_label
        val dialogNegativeBtnLabelRes = R.string.timer_stop_action_choice_dialog_negative_action_btn_label

        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(dialogTitleRes)
            .setMessage(dialogMessageRes)
            .setPositiveButton(dialogPositiveBtnLabelRes) { _, _ ->
                viewModel.onTimerResetActionSelected()
            }
            .setNegativeButton(dialogNegativeBtnLabelRes) { dialogInterface: DialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }
}