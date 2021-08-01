package kr.valor.bal.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.adapters.ShowDetailInfoListener
import kr.valor.bal.adapters.home.HomeAdapter
import kr.valor.bal.databinding.FragmentHomeBinding
import kr.valor.bal.utilities.observeInLifecycle

// TODO : Issue #3
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.userRecordsView.adapter = HomeAdapter(
            ShowDetailInfoListener { Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show() }
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupEventObserver()
    }

    private fun setupEventObserver() {
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
}