package kr.valor.bal.ui.schedule

import android.os.Bundle
import android.view.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
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
class ScheduleFragment : DialogContainerFragment() {

    private val viewModel: ScheduleViewModel by viewModels()

    private lateinit var binding: ViewDataBinding

    private lateinit var scheduleAdapter: ScheduleAdapter

    private lateinit var detailAdapter: DetailAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        return when(MainApplication.prefs.getWorkoutRecordingState()) {
            false -> ScheduleFragmentBinding.inflate(inflater, container, false)
            true -> ScheduleDoneFragmentBinding.inflate(inflater, container, false)
        }.also {
            binding = it
            when(it) {
                is ScheduleFragmentBinding -> it.initRecordLayoutBinding()
                is ScheduleDoneFragmentBinding -> it.initDoneLayoutBinding()
            }
        }.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        when(binding) {
            is ScheduleDoneFragmentBinding -> inflater.inflate(R.menu.schedule_done_menu, menu)
            is ScheduleFragmentBinding -> inflater.inflate(R.menu.schedule_recording_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.edit_schedule_menu_item -> {
                viewModel.onEditWorkoutButtonClicked()
                return true
            }
            R.id.add_new_workout_menu_item -> {
                viewModel.onAddNewWorkoutButtonClicked()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLiveDataObserver()
        setEventObserver()
    }

    private fun setLiveDataObserver() {
        viewModel.currentWorkoutSchedule.observe(viewLifecycleOwner) { schedule ->
            when (binding) {
                is ScheduleFragmentBinding -> {
                    val list = WorkoutDetailItem.convertToNoHeaderAdapterList(schedule)
                    scheduleAdapter.submitList(list)
                }
                is ScheduleDoneFragmentBinding -> {
                    val list = WorkoutDetailItem.convertToRequireHeaderAdapterList(schedule)
                    detailAdapter.submitList(list)
                }
            }
        }
    }

    private fun setEventObserver() {
        viewModel.eventsFlow
            .onEach {
                when (it) {
                    ScheduleViewModel.Event.ShowAddNewWorkoutDialog -> {
                        showWorkoutSelectionDialog(viewModel)
                    }
                    ScheduleViewModel.Event.ShowTimerStopActionChoiceDialog -> {
                        showTimerResetActionChoiceDialog {
                            viewModel.onTimerResetActionSelected()
                        }
                    }
                    ScheduleViewModel.Event.ShowEditActionChoiceDialog -> {
                        showEditActionChoiceDialog {
                            viewModel.onEditWorkoutAcceptClicked()
                        }
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

    private fun ScheduleFragmentBinding.initRecordLayoutBinding() {
        bindingCreator = WorkoutSummaryInfoBindingParameterCreator
        viewModel = this@ScheduleFragment.viewModel
        lifecycleOwner = viewLifecycleOwner

        recyclerView = scheduleRecyclerView.also { it.initRecyclerview(this) }
    }

    private fun ScheduleDoneFragmentBinding.initDoneLayoutBinding() {
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

    private fun RecyclerView.initRecyclerview(binding: ScheduleFragmentBinding) {

        scheduleAdapter = ScheduleAdapter(*initializeRecyclerviewClickListeners())
            .also { adapter = it }

        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
//        (itemAnimator as SimpleItemAnimator).moveDuration = 2000L
//        (itemAnimator as SimpleItemAnimator).removeDuration = 2000L
//        (itemAnimator as SimpleItemAnimator).addDuration = 2000L
//        (itemAnimator as SimpleItemAnimator).changeDuration = 2000L
//        addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy > 0) {
//                    binding.addWorkoutButton.hide()
//                } else {
//                    binding.addWorkoutButton.show()
//                }
//            }
//        })
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

}