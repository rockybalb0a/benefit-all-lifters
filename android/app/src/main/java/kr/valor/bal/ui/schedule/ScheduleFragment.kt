package kr.valor.bal.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.R
import kr.valor.bal.adapters.ScheduleAdapter
import kr.valor.bal.adapters.listeners.ScheduleButtonListener
import kr.valor.bal.adapters.listeners.ScheduleFinishListener
import kr.valor.bal.adapters.listeners.ScheduleSetListener
import kr.valor.bal.adapters.listeners.WorkoutDetailItem
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.databinding.ScheduleFragmentBinding
import kr.valor.bal.utilities.elapsedTimeFormatter

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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = ScheduleAdapter(
            addClickListener = ScheduleButtonListener { item ->
                viewModel.onAddNewSetButtonClicked(item.workoutDetail.detailId)
            },
            deleteClickListener = ScheduleButtonListener { item ->
                viewModel.onDeleteSetButtonClicked(item.workoutDetail.detailId)
            },
            closeClickListener = ScheduleButtonListener { item ->
                viewModel.onCloseButtonClicked(item.workoutDetail)
            },
            setClickListener = ScheduleSetListener { item ->
                findNavController().navigate(ScheduleFragmentDirections.actionScheduleDestToScheduleSetDialogFragment(item.setId))
            },
            finishedClickListener = ScheduleFinishListener {
                viewModel.onWorkoutFinished()
            }
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

        // TODO : Using proper fragment and viewmodel with safe args
        // TODO : How to hide bottom navigation view?
        // TODO : Refer this : https://developer.android.com/guide/navigation/navigation-ui#listen_for_navigation_events
        // TODO : How to add workout set? https://stackoverflow.com/questions/31446779/android-databinding-dynamic-addview/36107367
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
                    WorkoutDetailItem.WorkoutDetailAndSetsItem(item)
                } + listOf(WorkoutDetailItem.Footer)
            adapter.submitList(items)
        }

    }

}