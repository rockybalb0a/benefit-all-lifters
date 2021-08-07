package kr.valor.bal.onboarding.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kr.valor.bal.databinding.OnBoardingPageHeaderBinding
import kr.valor.bal.onboarding.OnBoardingViewModel

class OnBoardingHeaderPage: Fragment() {
    private val sharedViewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = OnBoardingPageHeaderBinding.inflate(inflater, container, false)

        return binding.root
    }
}