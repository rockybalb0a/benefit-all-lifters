package kr.valor.bal.ui.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.adapters.overview.OverviewAdapter
import kr.valor.bal.adapters.OverviewItemListener
import kr.valor.bal.databinding.FragmentOverviewBinding
import kr.valor.bal.utilities.observeInLifecycle


@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentOverviewBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = OverviewAdapter(OverviewItemListener {
            viewModel.onOverviewItemClicked(it.workoutOverview.overviewId)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.eventsFlow.onEach {
            when(it) {
                is OverviewViewModel.Event.NavigateToDetailDest -> {
                    findNavController().navigate(
                        OverviewFragmentDirections.actionOverviewDestToScheduleDetailDest(
                            it.overviewId
                        )
                    )
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }
}