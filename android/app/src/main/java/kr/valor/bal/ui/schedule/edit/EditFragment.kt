package kr.valor.bal.ui.schedule.edit

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.R
import kr.valor.bal.adapters.*
import kr.valor.bal.adapters.schedule.ScheduleAdapter
import kr.valor.bal.databinding.EditFragmentBinding
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator
import kr.valor.bal.utilities.observeInLifecycle


@AndroidEntryPoint
class EditFragment : Fragment() {

    private val viewModel: EditViewModel by viewModels()

    private val navArgs by navArgs<EditFragmentArgs>()

    private lateinit var binding: EditFragmentBinding

    private lateinit var editAdapter: ScheduleAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return EditFragmentBinding.inflate(inflater, container, false).also {
            binding = it
            binding.initBinding()
        }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBackButtonPressedCallback()
        binding.initBinding()

        viewModel.currentWorkoutSchedule.observe(viewLifecycleOwner) { schedule ->
            Log.d("update-after", "${schedule.workoutOverview.elapsedTimeMilli}")

            val items =
                listOf(WorkoutDetailItem.Header(schedule.workoutOverview)) +
                        schedule.workoutDetails.map { item ->
                            WorkoutDetailItem.Item(item)
                        } + listOf(WorkoutDetailItem.Footer)
            editAdapter.submitList(items)
        }

        viewModel.eventsFlow
            .onEach { event ->
                when(event) {
                    is EditViewModel.Event.EditDoneAndBackToDetailDest ->
                        findNavController().navigate(
                            EditFragmentDirections.actionEditDestToScheduleDetailDest(
                                event.overviewId
                            )
                        )

                    is EditViewModel.Event.EditRejectAndBackToDetailDest ->
                        findNavController().navigate(
                            EditFragmentDirections.actionEditDestToScheduleDetailDest(
                                event.overviewId
                            )
                        )

                    is EditViewModel.Event.ShowAddNewWorkoutDialog -> showWorkoutSelectionDialog()

                    is EditViewModel.Event.ShowTimerSettingDialog -> showTimerManualSettingDialog()

                    is EditViewModel.Event.EditDetectedEvent -> showApplyChangeChoiceActionDialog()
                }
            }
            .observeInLifecycle(viewLifecycleOwner)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.schedule_recording_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_new_workout_menu_item -> {
                viewModel.onAddNewWorkoutButtonClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun EditFragmentBinding.initBinding() {
        setHasOptionsMenu(true)

        viewModel = this@EditFragment.viewModel
        bindingCreator = WorkoutSummaryInfoBindingParameterCreator
        lifecycleOwner = viewLifecycleOwner
        recyclerView = scheduleRecyclerView.also { it.bindingRecyclerView() }
    }

    private fun initBackButtonPressedCallback() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackButtonClicked()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

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

    private fun showApplyChangeChoiceActionDialog(): AlertDialog {
        val dialogTitleRes = R.string.save_changes_action_choice_dialog_title
        val dialogMessageRes = R.string.save_changes_action_choice_dialog_message
        val dialogPositiveBtnLabelRes = R.string.save_changes_action_choice_dialog_positive_action_btn_label
        val dialogNegativeBtnLabelRes = R.string.save_changes_action_choice_dialog_negative_action_btn_label

        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(dialogTitleRes)
            .setMessage(dialogMessageRes)
            .setPositiveButton(dialogPositiveBtnLabelRes) { _,_ ->
                findNavController().navigate(
                   EditFragmentDirections.actionEditDestToScheduleDetailDest(
                        navArgs.overviewId
                    )
                )
            }
            .setNegativeButton(dialogNegativeBtnLabelRes) {_, _ ->
                viewModel.onEditRejectButtonClicked()
            }
            .show()
    }

    // TODO Timer picker spinner mode
    private fun showTimerManualSettingDialog(): AlertDialog {
        val dialogTitleRes = R.string.save_changes_action_choice_dialog_title
        val dialogMessageRes = R.string.save_changes_action_choice_dialog_message
        val dialogPositiveBtnLabelRes = R.string.save_changes_action_choice_dialog_positive_action_btn_label
        val dialogNegativeBtnLabelRes = R.string.save_changes_action_choice_dialog_negative_action_btn_label

        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(dialogTitleRes)
            .setMessage(dialogMessageRes)
            .setPositiveButton(dialogPositiveBtnLabelRes) { _,_ ->
                viewModel.onEditTimerButtonClicked(5)
            }
            .setNegativeButton(dialogNegativeBtnLabelRes) {_, _ ->
                viewModel.onEditTimerButtonClicked(-5)
            }
            .show()
    }

    private fun RecyclerView.bindingRecyclerView() {
        editAdapter = ScheduleAdapter(*initializeRecyclerviewClickListeners())
            .also { adapter = it }
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
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
                    .navigate(
                        EditFragmentDirections.actionEditDestToScheduleSetDest(
                            item.setId
                        )
                    )
            },

            CompleteWorkoutScheduleListener {
                viewModel.onEditFinishButtonClicked()
            },

            ManualTimerSettingListener {
                viewModel.onEditWorkoutTimerSettingButtonClicked()
            }
        )
}