package kr.valor.bal

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.valor.bal.data.local.user.UserDao
import kr.valor.bal.data.local.user.UserInfo
import kr.valor.bal.data.local.user.UserPersonalRecording
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
        setContentView(binding.root)

        viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(viewPager.currentItem) {
                    HEADER -> {}
                    FOOTER -> sharedViewModel.gatheringUserPrInfo()
                    else -> sharedViewModel.onViewChanged(position-1)
                }
            }
        })

        TabLayoutMediator(binding.tabIndicator, viewPager) {_,_ ->}.attach()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    companion object {
        private const val HEADER = 0
        private const val FOOTER = 6
    }

}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val app: Application,
    private val userDao: UserDao
): AndroidViewModel(app) {
    private lateinit var _userInfo: UserInfo

    private var _currentViewPosition: Int = Int.MAX_VALUE

    private val _userPrRecordingList = mutableListOf<UserPersonalRecording>()
    private val _userPrRecordingListLiveData = MutableLiveData<List<UserPersonalRecording>>()
    val userPrRecordingListLiveData: LiveData<List<UserPersonalRecording>>
        get() = _userPrRecordingListLiveData

    private val _userPrRecording = MutableLiveData<UserPersonalRecording>()
    val userPrRecording: LiveData<UserPersonalRecording>
        get() = _userPrRecording

    val inputText = MutableLiveData<String>()

    init {
        initUserPrRecordingContainer()
    }

    fun onViewChanged(position: Int) {
        _userPrRecording.value = _userPrRecordingList[position]
        _currentViewPosition = position
        inputText.value =
            if (_userPrRecording.value!!.weights % 1.0 == 0.0)
                _userPrRecording.value!!.weights.toInt().toString()
            else
                _userPrRecording.value!!.weights.toString()
    }

    fun storeWeightsInput() {
        val inputWeights = if (inputText.value.isNullOrEmpty()) 0.0 else inputText.value!!.toDouble()
        _userPrRecordingList[_currentViewPosition].weights = inputWeights
        _userPrRecording.value = _userPrRecordingList[_currentViewPosition]
    }

    fun onPlusWeightsButtonClicked() {
        val inputWeights = inputText.value!!.toDouble() + 0.5
        inputText.value = if (inputWeights % 1.0 == 0.0) inputWeights.toInt().toString() else inputWeights.toString()
    }

    fun onMinusWeightsButtonClicked() {
        val inputWeights = inputText.value!!.toDouble() - 0.5
        if (inputWeights < 0.0) return
        inputText.value = if (inputWeights % 1.0 == 0.0) inputWeights.toInt().toString() else inputWeights.toString()
    }

    fun onPlusRepsButtonClicked() {
        _userPrRecordingList[_currentViewPosition].reps += 1
        _userPrRecording.value = _userPrRecordingList[_currentViewPosition]
    }

    fun onMinusRepsButtonClicked() {
        if (_userPrRecordingList[_currentViewPosition].reps == 0) return
        _userPrRecordingList[_currentViewPosition].reps -= 1
        _userPrRecording.value = _userPrRecordingList[_currentViewPosition]
    }

    fun gatheringUserPrInfo() {
        _userPrRecordingListLiveData.value =
            _userPrRecordingList.toList()
    }

    fun createUserInfo() {

    }

    fun doneInitialSetting() {
        insertUserInfo()
    }

    private fun insertUserInfo() {
        viewModelScope.launch {
            userDao.insertUserInfo(_userInfo)
        }
    }

    private fun initUserPrRecordingContainer() {
        val workoutList = app.resources.getStringArray(R.array.exercise_list)
        val initialDataList = listOf(
            UserPersonalRecording(workoutName = workoutList[0]),
            UserPersonalRecording(workoutName = workoutList[1]),
            UserPersonalRecording(workoutName = workoutList[2]),
            UserPersonalRecording(workoutName = workoutList[3]),
            UserPersonalRecording(workoutName = workoutList[4])
        )
        _userPrRecordingList.addAll(initialDataList)
    }
}

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    companion object {
        private const val PAGE_COUNT = 7
        private const val HEADER = 0
        private const val FOOTER = 6
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

    // See More : Android button has setOnTouchListener called on it but does not override performClick
    // https://stackoverflow.com/questions/47107105/android-button-has-setontouchlistener-called-on-it-but-does-not-override-perform
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = ViewPagerContentBinding.inflate(inflater, container, false)
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


class ViewPagerFooter(): Fragment() {
    private val sharedViewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = ViewPagerFooterBinding.inflate(inflater, container, false)

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
