package kr.valor.bal.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.R
import kr.valor.bal.adapters.*
import kr.valor.bal.adapters.schedule.ScheduleAdapter
import kr.valor.bal.databinding.ScheduleFragmentBinding
import kr.valor.bal.utilities.binding.ScheduleBindingParameterCreator

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private val viewModel: ScheduleViewModel by viewModels()

    private lateinit var binding: ScheduleFragmentBinding

    private lateinit var adapter: ScheduleAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ScheduleFragmentBinding.inflate(inflater, container, false)
        binding.bindingCreator = ScheduleBindingParameterCreator
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = ScheduleAdapter(
            *initializeRecyclerviewClickListeners()
        )

        recyclerView = binding.scheduleRecyclerView
        recyclerView.adapter = adapter

        // TODO : Considering use Coordinator layout or Motion layout
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding.addWorkoutButton.hide()
                } else {
                    binding.addWorkoutButton.show()
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exercises = resources.getStringArray(R.array.exercise_list)

        binding.addWorkoutButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.add_new_workout_popup_title)
                .setItems(exercises) { _, idx ->
                    viewModel.onDialogItemSelected(exercises[idx])
                }
                .show()
        }

        viewModel.currentWorkoutSchedule.observe(viewLifecycleOwner) {
            val items =
                it.workoutDetails.map { item ->
                    WorkoutDetailItem.Item(item)
                } + listOf(WorkoutDetailItem.Footer)
            adapter.submitList(items)
        }
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
                viewModel.onWorkoutFinish()
            }
        )

}