package kr.valor.bal.ui.schedule.edit

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.R
import kr.valor.bal.adapters.*
import kr.valor.bal.adapters.schedule.ScheduleAdapter
import kr.valor.bal.databinding.EditFragmentBinding
import kr.valor.bal.ui.schedule.DialogContainerFragment
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator
import kr.valor.bal.utilities.observeInLifecycle


@AndroidEntryPoint
class EditFragment : DialogContainerFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBackButtonPressedCallback()
        binding.initBinding()
        setLiveDataObserver()
        setEventObserver()
    }

    private fun setLiveDataObserver() {
        viewModel.currentWorkoutSchedule.observe(viewLifecycleOwner) { schedule ->
            val items = WorkoutDetailItem.convertToRequireHeaderAdapterList(schedule)
            editAdapter.submitList(items)
        }
    }

    private fun setEventObserver() {
        viewModel.eventsFlow
            .onEach { event ->
                when (event) {
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
                    is EditViewModel.Event.EditDetectedEvent -> showApplyChangeChoiceActionDialog(
                        positiveAction = {
                            findNavController().navigate(
                                EditFragmentDirections
                                    .actionEditDestToScheduleDetailDest(navArgs.overviewId)
                            )
                        },
                        negativeAction = {
                            viewModel.onEditRejectButtonClicked()
                        }
                    )
                    is EditViewModel.Event.ShowAddNewWorkoutDialog -> showWorkoutSelectionDialog(viewModel)
                    is EditViewModel.Event.ShowTimerSettingDialog -> {
                        //                        showTimerManualSettingDialog()
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)
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