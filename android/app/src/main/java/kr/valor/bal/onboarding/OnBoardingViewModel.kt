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
import kr.valor.bal.utilities.convertToSimpleNumericString
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val app: Application,
    private val userDao: UserDao
): AndroidViewModel(app) {
    private lateinit var _userInfo: UserInfo

    private var currentViewPosition: Int = Int.MAX_VALUE

    private val onBoardingContentList = mutableListOf<OnBoardingContent>()
    private val _onBoardingContentListLiveData = MutableLiveData<List<OnBoardingContent>>()
    val onBoardingContentListLiveData: LiveData<List<OnBoardingContent>>
        get() = _onBoardingContentListLiveData

    private val _onBoardingContent = MutableLiveData<OnBoardingContent>()
    val onBoardingContent: LiveData<OnBoardingContent>
        get() = _onBoardingContent

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