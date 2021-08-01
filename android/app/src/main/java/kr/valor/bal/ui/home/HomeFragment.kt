package kr.valor.bal.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.R
import kr.valor.bal.adapters.ShowDetailInfoListener
import kr.valor.bal.adapters.home.HomeAdapter
import kr.valor.bal.data.WorkoutSummaryInfo
import kr.valor.bal.databinding.FragmentHomeBinding
import kr.valor.bal.utilities.observeInLifecycle


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    private lateinit var userRecordsRecyclerView: RecyclerView

    private lateinit var userRecordsAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.initBinding()


        viewModel.eventsFlow
            .onEach {
                when (it) {
                    HomeViewModel.Event.NavigateToScheduleDest -> {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeDestToScheduleDest()
                        )
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)
    }

    private fun FragmentHomeBinding.initBinding() {
        viewModel = this@HomeFragment.viewModel
        lifecycleOwner = viewLifecycleOwner
        userRecordsRecyclerView = userRecordsView.also { it.initRecyclerView() }
    }

    private fun RecyclerView.initRecyclerView() {
        userRecordsAdapter = HomeAdapter(
            ShowDetailInfoListener { Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show() }
        ).also {
            adapter = it
        }
        val dummyList = createDummyData()
        userRecordsAdapter.submitList(dummyList)
    }

    private fun createDummyData(): List<WorkoutSummaryInfo> {
        val workoutList = resources.getStringArray(R.array.exercise_list)
        val workoutSummaryInfoList = mutableListOf<WorkoutSummaryInfo>()
        workoutList.forEach {
            workoutSummaryInfoList.add(
                WorkoutSummaryInfo(it, 100.0)
            )
        }
        return workoutSummaryInfoList.toList()
    }
}