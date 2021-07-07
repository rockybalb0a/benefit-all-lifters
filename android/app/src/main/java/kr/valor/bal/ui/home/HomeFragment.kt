package kr.valor.bal.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.R
import kr.valor.bal.databinding.HomeFragmentBinding
import kr.valor.bal.utilities.observeInLifecycle


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return HomeFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun HomeFragmentBinding.initBinding() {
        viewModel = this@HomeFragment.viewModel
        lifecycleOwner = viewLifecycleOwner
    }
}