package kr.valor.bal.ui.overview.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.adapters.overview.detail.DetailAdapter
import kr.valor.bal.databinding.DetailFragmentBinding


// TODO : Considering use of ViewPager2 ... ?
// TODO : Upgrading UI needed
@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()

    private lateinit var binding: DetailFragmentBinding

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: DetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)

        adapter = DetailAdapter()

        recyclerView = binding.recyclerView

        recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.workoutSchedule.observe(viewLifecycleOwner) {
            (activity as AppCompatActivity).supportActionBar?.title = it.workoutOverview.date.toString()
            adapter.submitList(it.workoutDetails)

        }
    }


}