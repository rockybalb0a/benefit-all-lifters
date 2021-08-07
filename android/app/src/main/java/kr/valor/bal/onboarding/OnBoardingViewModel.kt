package kr.valor.bal.onboarding

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.valor.bal.R
import kr.valor.bal.data.local.user.UserDao
import kr.valor.bal.data.local.user.UserInfo
import kr.valor.bal.data.local.user.UserPersonalRecording
import kr.valor.bal.utilities.convertToSimpleNumericString
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val app: Application,
    private val userDao: UserDao
): AndroidViewModel(app) {

    sealed class Event {
        object ShowDatePickerEvent: Event()
    }

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventFlow: Flow<Event>
        get() = _eventChannel.receiveAsFlow()

    private lateinit var _userInfo: UserInfo

    private var currentViewPosition: Int = Int.MAX_VALUE

    private val onBoardingContentList = mutableListOf<OnBoardingContent>()
    private val _onBoardingContentListLiveData = MutableLiveData<List<OnBoardingContent>>()
    val onBoardingContentListLiveData: LiveData<List<OnBoardingContent>>
        get() = _onBoardingContentListLiveData

    private val _onBoardingContent = MutableLiveData<OnBoardingContent>()
    val onBoardingContent: LiveData<OnBoardingContent>
        get() = _onBoardingContent

    private val _datePicked = MutableLiveData<Long?>()
    private val _datePickedToLocalDate = _datePicked.map {
        it?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
    val datePicked: LiveData<String>
        get() = _datePicked.map {
            it?.let {
                val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                localDate.format(DateTimeFormatter.ISO_DATE)
            } ?: "Not Chosen"
        }

    val inputText = MutableLiveData<String>()

    init {
        initOnBoardingInfo()
    }

    fun onViewChanged(position: Int) {
        _onBoardingContent.value = onBoardingContentList[position]
        currentViewPosition = position
        inputText.value =
            _onBoardingContent.value?.prInfo?.weights.convertToSimpleNumericString()
    }

    fun storeWeightsInput() {
        val inputWeights = if (inputText.value.isNullOrEmpty()) 0.0 else inputText.value!!.toDouble()
        onBoardingContentList[currentViewPosition].prInfo.weights = inputWeights
        _onBoardingContent.value = onBoardingContentList[currentViewPosition]
    }

    fun onDateChooseButtonClicked() {
        viewModelScope.launch {
            _eventChannel.send(Event.ShowDatePickerEvent)
        }
    }

    fun onDateSelected(milli: Long?) {
        _datePicked.value = milli
    }

    fun onPlusWeightsButtonClicked() {
        val inputWeights = inputText.value!!.toDouble() + 0.5
        inputText.value = inputWeights.convertToSimpleNumericString()
    }

    fun onMinusWeightsButtonClicked() {
        val inputWeights = inputText.value!!.toDouble() - 0.5
        if (inputWeights < 0.0) return
        inputText.value = inputWeights.convertToSimpleNumericString()
    }

    fun onPlusRepsButtonClicked() {
        onBoardingContentList[currentViewPosition].prInfo.reps += 1
        _onBoardingContent.value = onBoardingContentList[currentViewPosition]
    }

    fun onMinusRepsButtonClicked() {
        onBoardingContentList[currentViewPosition].prInfo.apply {
            if (this.reps == 0) {
                return@apply
            } else {
                this.reps -= 1
                _onBoardingContent.value = onBoardingContentList[currentViewPosition]
            }
        }

    }

    fun gatheringUserPrInfo() {
        _onBoardingContentListLiveData.value =
            onBoardingContentList.toList()
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

    private fun initOnBoardingInfo() {
        val workoutList = app.resources.getStringArray(R.array.exercise_list)
        val contentList: MutableList<OnBoardingContent> = mutableListOf()

        workoutList.forEachIndexed { idx, workoutName ->
            OnBoardingContent(
                contentTitle = app.getString(
                    R.string.on_boarding_instruction_title,
                    idx + 1,
                    workoutName.lowercase().replaceFirstChar { it.uppercase() }
                ),
                contentSubTitle = app.getString(R.string.on_boarding_instruction_sub_title, workoutName.lowercase()),
                contentDescription = app.getString(R.string.on_boarding_instruction_body, workoutName.lowercase()),
                UserPersonalRecording(workoutName = workoutName)
            )
                .also { contentList.add(it) }
        }

        onBoardingContentList.addAll(contentList)
    }

    data class OnBoardingContent(
        val contentTitle: String,
        val contentSubTitle: String,
        val contentDescription: String,
        val prInfo: UserPersonalRecording
    )
}