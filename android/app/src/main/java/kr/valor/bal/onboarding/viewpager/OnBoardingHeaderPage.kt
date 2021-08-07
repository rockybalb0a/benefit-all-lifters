package kr.valor.bal.onboarding.viewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.onEach
import kr.valor.bal.R
import kr.valor.bal.databinding.OnBoardingPageHeaderBinding
import kr.valor.bal.onboarding.OnBoardingViewModel
import kr.valor.bal.utilities.observeInLifecycle
import java.time.LocalDateTime

class OnBoardingHeaderPage: Fragment() {
    private val sharedViewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = OnBoardingPageHeaderBinding.inflate(inflater, container, false)

        binding.viewModel = sharedViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpEventObserver()
    }

    private fun setUpEventObserver() {
        sharedViewModel.eventFlow.onEach {
            when(it) {
                is OnBoardingViewModel.Event.ShowDatePickerEvent -> {
                    showDatePicker()
                }
            }
        }
            .observeInLifecycle(viewLifecycleOwner)
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
        datePicker.show(parentFragmentManager, null)
    }

}