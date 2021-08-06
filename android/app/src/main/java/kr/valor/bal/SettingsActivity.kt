package kr.valor.bal

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDeepLinkBuilder
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.valor.bal.databinding.ActivitySettingsBinding
import kr.valor.bal.databinding.ViewPagerContentBinding
import kr.valor.bal.databinding.ViewPagerFooterBinding
import kr.valor.bal.databinding.ViewPagerHeaderBinding
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity: AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    private val sharedViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)

        viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabIndicator, viewPager) {_,_ ->}.attach()

        setContentView(binding.root)
    }
}

@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel()

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    companion object {
        private const val PAGE_COUNT = 6
        private const val HEADER = 0
        private const val FOOTER = 5
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            HEADER -> ViewPagerHeader()
            FOOTER -> ViewPagerFooter()
            else -> ViewPagerContent(position)
        }
    }
}

class ViewPagerHeader(): Fragment() {
    private val sharedViewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = ViewPagerHeaderBinding.inflate(inflater, container, false)

        return binding.root
    }
}

class ViewPagerContent(private val position: Int): Fragment() {

    private val sharedViewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = ViewPagerContentBinding.inflate(inflater, container, false)
        binding.title.text = position.toString()
        return binding.root
    }
}


class ViewPagerFooter(): Fragment() {
    private val sharedViewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = ViewPagerFooterBinding.inflate(inflater, container, false)
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
