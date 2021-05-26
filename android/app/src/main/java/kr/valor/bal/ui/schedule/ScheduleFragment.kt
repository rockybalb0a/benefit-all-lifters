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
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.R
import kr.valor.bal.adapters.ScheduleAdapter
import kr.valor.bal.adapters.ScheduleItemListener
import kr.valor.bal.databinding.FragmentScheduleBinding

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private val viewModel: ScheduleViewModel by viewModels()

    private lateinit var binding: FragmentScheduleBinding

    private lateinit var adapter: ScheduleAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)

        adapter = ScheduleAdapter(
            // TODO : Defining proper click listener
            ScheduleItemListener { item ->
                Toast.makeText(requireContext(), item.workoutDetail.workoutName, Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView = binding.scheduleRecyclerView
        recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exercises = resources.getStringArray(R.array.exercise_list)

        // TODO : Using proper fragment and viewmodel with safe args
        // TODO : How to hide bottom navigation view?
        // TODO : Refer this : https://developer.android.com/guide/navigation/navigation-ui#listen_for_navigation_events
        binding.addWorkoutButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.add_new_workout_popup_title)
                .setItems(exercises) { _, idx ->
                    viewModel.onDialogItemSelected(exercises[idx])
                }
                .show()
//            findNavController().navigate(R.id.action_schedule_dest_to_schedule_dialog_dest)
        }

        viewModel.currentWorkoutSchedule.observe(viewLifecycleOwner) {
            if (it.workoutDetails.isNotEmpty()) {
                binding.scheduleRecyclerView.visibility = View.VISIBLE
                binding.scheduleEmptyPlaceholder.visibility = View.GONE
                adapter.submitList(it.workoutDetails)
            }
        }

    }
}