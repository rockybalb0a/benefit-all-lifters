package kr.valor.bal.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.R
import kr.valor.bal.adapters.detail.DetailAdapter
import kr.valor.bal.databinding.ScheduleDetailFragmentBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator
import kr.valor.bal.utilities.observeInLifecycle

@AndroidEntryPoint
class ScheduleDetailFragment: Fragment() {

    private val viewModel: ScheduleDetailViewModel by viewModels()

    private lateinit var binding: ScheduleDetailFragmentBinding

    private lateinit var contentRecyclerView: RecyclerView

    private lateinit var adapter: DetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ScheduleDetailFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initBinding()
        contentRecyclerView = binding.contentsRecyclerView.also {
            adapter = DetailAdapter()
            it.adapter = adapter
        }

        viewModel.workoutSchedule.observe(viewLifecycleOwner) {
            adapter.submitList(it.workoutDetails)
        }

        viewModel.eventFlow
            .onEach {
                when (it) {
                    ScheduleDetailViewModel.Event.NavigateToOverviewDest -> {
                        findNavController().navigate(
                            ScheduleDetailFragmentDirections.actionScheduleDetailDestToOverviewDest()
                        )
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)

    }


    private fun ScheduleDetailFragmentBinding.initBinding() {
        with(this) {
            viewModel = this@ScheduleDetailFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            headerBindingCreator = WorkoutSummaryInfoBindingParameterCreator
            contentBindingCreator = WorkoutDetailInfoBindingParameterCreator
        }
    }
}