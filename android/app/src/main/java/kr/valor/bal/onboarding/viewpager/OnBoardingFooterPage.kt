package kr.valor.bal.onboarding.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kr.valor.bal.databinding.OnBoardingPageFooterBinding
import kr.valor.bal.onboarding.OnBoardingViewModel

class OnBoardingFooterPage: Fragment() {
    private val sharedViewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = OnBoardingPageFooterBinding.inflate(inflater, container, false)

        binding.viewModel = sharedViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.userPrRecyclerView.adapter = LastPageAdapter()

        return binding.root
    }

}
