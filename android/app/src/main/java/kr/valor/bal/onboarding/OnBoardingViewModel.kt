package kr.valor.bal.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.valor.bal.R
import kr.valor.bal.data.local.user.UserDao
import kr.valor.bal.data.local.user.UserInfo
import kr.valor.bal.data.local.user.UserPersonalRecording
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
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