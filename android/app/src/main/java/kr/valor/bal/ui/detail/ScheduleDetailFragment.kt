package kr.valor.bal.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kr.valor.bal.adapters.WorkoutDetailItem
import kr.valor.bal.adapters.detail.DetailAdapter
import kr.valor.bal.databinding.FragmentDetailBinding
import kr.valor.bal.utilities.binding.WorkoutDetailInfoBindingParameterCreator
import kr.valor.bal.utilities.binding.WorkoutSummaryInfoBindingParameterCreator
import kr.valor.bal.utilities.observeInLifecycle

@AndroidEntryPoint
class ScheduleDetailFragment: Fragment() {

    private val viewModel: ScheduleDetailViewModel by viewModels()

    private lateinit var binding: FragmentDetailBinding

    private lateinit var contentRecyclerView: RecyclerView

    private lateinit var adapter: DetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initBinding()
        contentRecyclerView = binding.contentsRecyclerView.also {
            adapter = DetailAdapter(null)
            it.adapter = adapter
        }

        viewModel.workoutSchedule.observe(viewLifecycleOwner) {
            val items = it.workoutDetails.map { item ->
                WorkoutDetailItem.Item(item)
            }
            adapter.submitList(items)
        }

//        viewModel.eventFlow
//            .onEach {
//                when (it) {
//                    ScheduleDetailViewModel.Event.NavigateToOverviewDest -> {
//                        findNavController().navigate(
//                            ScheduleDetailFragmentDirections.actionScheduleDetailDestToOverviewDest()
//                        )
//                    }
//                    is ScheduleDetailViewModel.Event.NavigateToEditDest -> {
//                        findNavController().navigate(
//                            ScheduleDetailFragmentDirections.actionScheduleDetailDestToEditDest(it.overviewId, it.date)
//                        )
//                    }
//                }
//            }
//            .observeInLifecycle(viewLifecycleOwner)

        
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.onEach {
                    when(it) {
                        ScheduleDetailViewModel.Event.NavigateToOverviewDest -> {
                            findNavController().navigate(
                                ScheduleDetailFragmentDirections.actionScheduleDetailDestToOverviewDest()
                            )
                        }
                        is ScheduleDetailViewModel.Event.NavigateToEditDest -> {
                            findNavController().navigate(
                                ScheduleDetailFragmentDirections.actionScheduleDetailDestToEditDest(it.overviewId, it.date)
                            )
                        }
                    }
                }.collect {  }
            }
        }

    }


    private fun FragmentDetailBinding.initBinding() {
        with(this) {
            viewModel = this@ScheduleDetailFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            headerBindingCreator = WorkoutSummaryInfoBindingParameterCreator
            contentBindingCreator = WorkoutDetailInfoBindingParameterCreator
        }
    }
}