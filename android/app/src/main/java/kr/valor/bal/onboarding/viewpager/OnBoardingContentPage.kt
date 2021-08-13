package kr.valor.bal.onboarding.viewpager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kr.valor.bal.databinding.OnBoardingPageContentBinding
import kr.valor.bal.onboarding.OnBoardingViewModel

class OnBoardingContentPage(): Fragment() {

    private val sharedViewModel: OnBoardingViewModel by activityViewModels()

    // See More : Android button has setOnTouchListener called on it but does not override performClick
    // https://stackoverflow.com/questions/47107105/android-button-has-setontouchlistener-called-on-it-but-does-not-override-perform
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = OnBoardingPageContentBinding.inflate(inflater, container, false)
        binding.viewModel = sharedViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        // See More : https://stackoverflow.com/questions/40452321/how-to-hide-keyboard-just-by-one-tap-outside-of-an-edittext
        binding.rootLayout.setOnTouchListener { _, _ ->
            binding.weightsInput.hideSoftKeyboardAndSaveState()
            true
        }

        // See More : https://stackoverflow.com/questions/8766781/how-to-remove-focus-from-edittext-when-user-is-done-editing/52984888
        binding.weightsInput.setOnKeyListener { editText, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                editText.hideSoftKeyboardAndSaveState()
                true
            } else {
                false
            }
        }
        return binding.root
    }

    private fun View.hideSoftKeyboardAndSaveState() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val windowToken = requireActivity().window.decorView.rootView.windowToken
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        isFocusable = false
        isFocusableInTouchMode = true
        sharedViewModel.storeWeightsInput()
    }
}