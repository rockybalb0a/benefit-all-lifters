package kr.valor.bal.ui.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.adapters.overview.OverviewAdapter
import kr.valor.bal.adapters.OverviewItemListener
import kr.valor.bal.databinding.FragmentOverviewBinding


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
        with(binding.recyclerView) {
            setHasFixedSize(true)
            adapter = OverviewAdapter(OverviewItemListener {
                findNavController().navigate(OverviewFragmentDirections
                    .actionOverviewDestToScheduleDetailDest(it.workoutOverview.overviewId))
            })
        }

        return binding.root
    }

}