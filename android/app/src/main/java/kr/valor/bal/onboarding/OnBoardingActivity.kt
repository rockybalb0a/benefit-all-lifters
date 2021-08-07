package kr.valor.bal.onboarding

import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.R
import kr.valor.bal.databinding.ActivitySettingsBinding
import kr.valor.bal.onboarding.viewpager.OnBoardingViewPagerAdapter
import kr.valor.bal.utilities.observeInLifecycle

@AndroidEntryPoint
class OnBoardingActivity: AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    private val sharedViewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewPager(binding)
        setUpEventObserver()
        TabLayoutMediator(binding.tabIndicator, viewPager) {_,_ ->}.attach()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    private fun setUpViewPager(binding: ActivitySettingsBinding) {

        viewPager = binding.viewPager

        with(viewPager) {
            adapter = OnBoardingViewPagerAdapter(this@OnBoardingActivity)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when(viewPager.currentItem) {
                        OnBoardingViewPagerAdapter.HEADER -> {}
                        OnBoardingViewPagerAdapter.FOOTER -> sharedViewModel.gatheringUserPrInfo()
                        else -> sharedViewModel.onPageChanged(position-1)
                    }
                }
            })
        }
    }

    private fun setUpEventObserver() {
        sharedViewModel.eventFlow.onEach {
            when(it) {
                OnBoardingViewModel.Request.ShowDatePicker -> {
                    showDatePicker()
                }
                OnBoardingViewModel.Request.NextStep -> {
                    viewPager.pageUp()
                }
                OnBoardingViewModel.Request.PreviousStep -> {
                    viewPager.pageDown()
                }
            }
        }
            .observeInLifecycle(this)
    }

    private fun showDatePicker() {
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraints)
                .setTitleText(R.string.on_boarding_welcome_date_picker_view_title)
                .build()
                .also {
                    it.addOnPositiveButtonClickListener { timeInMilli ->
                        sharedViewModel.onDateSelected(timeInMilli)
                    }
                    it.addOnNegativeButtonClickListener {
                        sharedViewModel.onDateSelected(null)
                    }
                }
        datePicker.show(supportFragmentManager, null)
    }


    private fun ViewPager2.pageUp() {
        currentItem += 1
    }
    private fun ViewPager2.pageDown() {
        currentItem -= 1
    }

}
