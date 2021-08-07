package kr.valor.bal.ui.home

import android.content.Intent
import android.net.Uri
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
import kr.valor.bal.adapters.home.VideoAdapter
import kr.valor.bal.adapters.home.VideoClickListener
import kr.valor.bal.databinding.FragmentHomeBinding
import kr.valor.bal.utilities.observeInLifecycle

// TODO : Issue #3
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.userRecordsView.adapter = HomeAdapter(
            ShowDetailInfoListener { Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show() }
        )
        // See More : https://stackoverflow.com/questions/54133757/recyclerview-not-showing-data-on-first-load
//        binding.userRecordsView.setHasFixedSize(true)

        binding.videoListView.adapter = VideoAdapter(
            VideoClickListener {
                val videoId = it.id
                val videoUri = "https://youtu.be/${videoId}"
                startActivity(Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse(videoUri))
                    .setPackage("com.google.android.youtube")
                )
            }
        )
        binding.videoListView.setHasFixedSize(true)

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
                    HomeViewModel.Event.EventNetworkError -> {
                        Toast.makeText(context, "Network Error !", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)
    }
}