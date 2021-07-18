package kr.valor.bal.ui.overview

import android.os.Bundle
import android.util.Log
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


// TODO : Set state for rest / workout
// TODO : Different UI for each state
@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModels()

    private lateinit var binding: OverviewFragmentBinding

    private lateinit var overviewAdapter: OverviewAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return OverviewFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView.also { it.initRecyclerView() }

        viewModel.workoutSchedules.observe(viewLifecycleOwner) {
            it?.let {
                overviewAdapter.submitList(it)
            }
        }
    }

    private fun RecyclerView.initRecyclerView() {
        setHasFixedSize(true)
        overviewAdapter = OverviewAdapter(
            OverviewItemListener {
//                findNavController().navigate(OverviewFragmentDirections
//                    .actionOverviewDestToDetailFragment(it.workoutOverview.overviewId))
                Log.d("NavArg", "${it.workoutOverview.overviewId}")
                findNavController().navigate(OverviewFragmentDirections
                    .actionOverviewDestToScheduleDetailDest(it.workoutOverview.overviewId))
            }
        ).also { adapter = it }
    }

}