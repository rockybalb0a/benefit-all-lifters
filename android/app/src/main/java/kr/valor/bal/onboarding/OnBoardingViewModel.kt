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
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val app: Application,
    private val userDao: UserDao
): AndroidViewModel(app) {

    sealed class Request {
        object ShowDatePicker: Request()
        object NextStep: Request()
        object PreviousStep: Request()
        object Finish: Request()
        object NavigateToHome: Request()
    }

    private val _eventChannel = Channel<Request>(Channel.BUFFERED)
    val eventFlow: Flow<Request>
        get() = _eventChannel.receiveAsFlow()

    private lateinit var _userInfo: UserInfo

    private var currentViewPosition: Int = Int.MAX_VALUE

    private val onBoardingContentList = mutableListOf<OnBoardingContent>()
    private val _onBoardingContentListLiveData = MutableLiveData<List<OnBoardingContent>>()
    val onBoardingContentListLiveData: LiveData<List<OnBoardingContent>>
        get() = _onBoardingContentListLiveData

    private val _userPrRecordingListLiveData= MutableLiveData<List<UserPersonalRecording>>()
    val userPrRecordingListLiveData: LiveData<List<UserPersonalRecording>>
        get() = _userPrRecordingListLiveData

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
    val headerNextStepButtonEnabled = _datePicked.map {
        it != null
    }

    val contentNextStepButtonText = _onBoardingContent.map {
        if (it.prInfo.weights == 0.0 || it.prInfo.reps == 0) {
            app.getString(R.string.on_boarding_skip_btn_label)
        } else {
            app.getString(R.string.on_boarding_next_btn_label)
        }
    }

    val inputText = MutableLiveData<String>()

    init {
        initOnBoardingInfo()
    }

    fun onPageChanged(position: Int) {
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
            _eventChannel.send(Request.ShowDatePicker)
        }
    }

    fun onNextStepButtonClicked() {
        viewModelScope.launch {
            _eventChannel.send(Request.NextStep)
        }
    }

    fun onPreviousStepButtonClicked() {
        viewModelScope.launch {
            _eventChannel.send(Request.PreviousStep)
        }
    }

    fun onFinishTutorialButtonClicked() {
        viewModelScope.launch {
            _eventChannel.send(Request.Finish)
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

        _userPrRecordingListLiveData.value =
            onBoardingContentList.map {
                it.prInfo
            }
    }

    fun finishOnBoardingProcess() {
        viewModelScope.launch {
            val userPrList = mutableListOf<UserPersonalRecording>()
            _userPrRecordingListLiveData.value!!.forEach {
                userPrList.add(it)
            }
            val date = _datePicked.value!!.let {
                val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                localDate
            }

            val newUserInfo = UserInfo(
                beginningOfWorkout = date,
                userPRList = userPrList
            )
            userDao.insertUserInfo(newUserInfo)
            _eventChannel.send(Request.NavigateToHome)
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