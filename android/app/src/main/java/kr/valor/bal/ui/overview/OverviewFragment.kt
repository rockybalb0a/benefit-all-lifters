package kr.valor.bal.ui.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.adapters.overview.OverviewAdapter
import kr.valor.bal.adapters.OverviewItemListener
import kr.valor.bal.databinding.OverviewFragmentBinding

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModels()

    private lateinit var adapter: OverviewAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = OverviewFragmentBinding.inflate(
            inflater, container, false
        )

        adapter = OverviewAdapter(
            OverviewItemListener {
                findNavController().navigate(OverviewFragmentDirections.actionOverviewDestToDetailFragment(it.workoutOverview.overviewId))
            }
        )
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.workoutSchedules.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }
    }

}