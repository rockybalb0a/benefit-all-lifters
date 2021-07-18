package kr.valor.bal.ui.schedule

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.MainApplication
import kr.valor.bal.R
import kr.valor.bal.adapters.*
import kr.valor.bal.adapters.detail.DetailAdapter
import kr.valor.bal.adapters.schedule.ScheduleAdapter
import kr.valor.bal.databinding.ScheduleDoneFragmentBinding
import kr.valor.bal.databinding.ScheduleFragmentBinding
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator
import kr.valor.bal.utilities.observeInLifecycle


@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private val viewModel: ScheduleViewModel by viewModels()

    private lateinit var binding: ViewDataBinding

    private lateinit var scheduleAdapter: ScheduleAdapter

    private lateinit var detailAdapter: DetailAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return when(MainApplication.prefs.getWorkoutRecordingState()) {
            false -> ScheduleFragmentBinding.inflate(inflater, container, false)
            true -> ScheduleDoneFragmentBinding.inflate(inflater, container, false)
        }.also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(binding) {
            is ScheduleFragmentBinding -> {
                (binding as ScheduleFragmentBinding).initBinding()
                viewModel.currentWorkoutSchedule.observe(viewLifecycleOwner) {
                    val items =
                        it.workoutDetails.map { item ->
                            WorkoutDetailItem.Item(item)
                        } + listOf(WorkoutDetailItem.Footer)
                    scheduleAdapter.submitList(items)
                }
            }

            is ScheduleDoneFragmentBinding -> {
                (binding as ScheduleDoneFragmentBinding).initBinding()
                viewModel.currentWorkoutSchedule.observe(viewLifecycleOwner) { schedule ->
                    val items =
                        listOf(WorkoutDetailItem.Header(schedule.workoutOverview)) +
                            schedule.workoutDetails.map { item ->
                                WorkoutDetailItem.Item(item)
                            } + listOf(WorkoutDetailItem.Footer)
                    detailAdapter.submitList(items)
                }
            }
        }

        setupFlowEventObserver()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(binding is ScheduleDoneFragmentBinding) {
            inflater.inflate(R.menu.menu_plan, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onEditWorkoutButtonClicked()
        return true
    }


    private fun ScheduleFragmentBinding.initBinding() {
        bindingCreator = WorkoutSummaryInfoBindingParameterCreator
        viewModel = this@ScheduleFragment.viewModel
        lifecycleOwner = viewLifecycleOwner

        recyclerView = scheduleRecyclerView.also { it.initRecyclerview(this) }
    }

    private fun ScheduleDoneFragmentBinding.initBinding() {
        setHasOptionsMenu(true)
        viewModel = this@ScheduleFragment.viewModel
        lifecycleOwner = viewLifecycleOwner
        recyclerView = detailRecyclerView.also {
            detailAdapter = DetailAdapter(EditWorkoutScheduleListener {
                this@ScheduleFragment.viewModel.onEditWorkoutButtonClicked()
            })
            it.setHasFixedSize(true)
            it.adapter = detailAdapter
        }
    }

    private fun setupFlowEventObserver() {

        viewModel.eventsFlow
            .onEach {
                when (it) {
                    ScheduleViewModel.Event.ShowAddNewWorkoutDialog -> {
                        showWorkoutSelectionDialog()
                    }
                    ScheduleViewModel.Event.ShowTimerStopActionChoiceDialog -> {
                        showTimerResetActionChoiceDialog()
                    }
                    ScheduleViewModel.Event.NavigateToScheduleDoneDest -> {
                        MainApplication.prefs.setWorkoutRecordingState(isCompleted = true)
                        findNavController().navigate(
                            ScheduleFragmentDirections.actionScheduleDestSelf()
                        )
                    }
                    ScheduleViewModel.Event.NavigateToScheduleEditDest -> {
                        MainApplication.prefs.setWorkoutRecordingState(isCompleted = false)
                        findNavController().navigate(
                            ScheduleFragmentDirections.actionScheduleDestSelf()
                        )
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)
    }


    private fun RecyclerView.initRecyclerview(binding: ScheduleFragmentBinding) {

        scheduleAdapter = ScheduleAdapter(*initializeRecyclerviewClickListeners())
            .also { adapter = it }
        setHasFixedSize(true)

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
                        .actionScheduleDestToScheduleSetDest(item.setId)
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