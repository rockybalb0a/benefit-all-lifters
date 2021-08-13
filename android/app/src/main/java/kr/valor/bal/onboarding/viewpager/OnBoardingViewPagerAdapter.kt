package kr.valor.bal.onboarding.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    companion object {
        const val PAGE_COUNT = 7
        const val HEADER = 0
        const val FOOTER = 6
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            HEADER -> OnBoardingHeaderPage()
            FOOTER -> OnBoardingFooterPage()
            else -> OnBoardingContentPage()
        }
    }
}