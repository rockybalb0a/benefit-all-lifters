package kr.valor.bal.onboarding.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDeepLinkBuilder
import kr.valor.bal.MainActivity
import kr.valor.bal.R
import kr.valor.bal.databinding.OnBoardingPageFooterBinding
import kr.valor.bal.onboarding.OnBoardingViewModel

class OnBoardingFooterPage(): Fragment() {
    private val sharedViewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = OnBoardingPageFooterBinding.inflate(inflater, container, false)

        binding.viewModel = sharedViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        sharedViewModel.userPrRecordingListLiveData.observe(viewLifecycleOwner) {
            var result = 0.0
            it.forEach { pr ->
                result += pr.weights
            }
            binding.footerTitle.text = result.toString()
        }

        binding.footerTitle.setOnClickListener {
            NavDeepLinkBuilder(requireActivity()).apply {
                setComponentName(MainActivity::class.java)
                setGraph(R.navigation.nav_graph)
                setDestination(R.id.home_dest)
                createPendingIntent().send()
            }
        }
        return binding.root
    }

}
