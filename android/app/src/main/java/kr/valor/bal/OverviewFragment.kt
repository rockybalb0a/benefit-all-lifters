package kr.valor.bal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kr.valor.bal.R
import kr.valor.bal.adapters.WorkoutOverviewAdapter
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.databinding.FragmentOverviewBinding
import kr.valor.bal.utilities.randomGenerator
import kr.valor.bal.viewmodels.OverviewViewModel
import java.time.LocalDate
import kotlin.random.Random

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModels()

    private lateinit var adapter: WorkoutOverviewAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOverviewBinding.inflate(
            inflater, container, false
        )

        adapter = WorkoutOverviewAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.workouts.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }
    }

}