package kr.valor.bal.ui.home

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.valor.bal.R
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.local.user.UserPersonalRecording
import kr.valor.bal.data.local.workout.WorkoutSchedule
import kr.valor.bal.utilities.TrackingStatus
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val repository: DefaultRepository
): AndroidViewModel(application) {

    sealed class Event {
        object NavigateToScheduleDest: Event()
        object EventNetworkError: Event()
    }
    private val res = application.resources

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow: Flow<Event>
        get() = _eventChannel.receiveAsFlow()

    private val _todayWorkoutSchedule = repository.workoutOverview.switchMap {
        it?.let { repository.getWorkoutScheduleByWorkoutOverviewId(it.overviewId) } ?: liveData { emit(null) }
    }

    private val _userInfo = repository.userInfo
    val totalDate = _userInfo.map {
        it?.let {
            val startDay = it.beginningOfWorkout
            val today = LocalDate.now()
            val dDayCount = ChronoUnit.DAYS.between(startDay, today) + 1
            application.getString(R.string.home_dDay_title, dDayCount)
        }
    }
    val fromDate = _userInfo.map {
        it?.let {
            application.getString(R.string.home_dDay_sub_title, it.beginningOfWorkout.format(DateTimeFormatter.ISO_DATE))
        }
    }

    private val _userPrRecords = MutableLiveData<List<UserPersonalRecording>>()
    val userPrRecords: LiveData<List<UserPersonalRecording>>
        get() = _userPrRecords

    val videos = repository.youtubeVideos

    init {
        refreshVideoFromRepository()
        // TODO : Real Implementation
        _userPrRecords.value = createDummyData()
    }

    val navigateButtonHeaderTitleText = _todayWorkoutSchedule.map {
        it.getResourcesByState(
            res.getString(R.string.home_nav_btn_header_title_if_exist),
            res.getString(R.string.home_nav_btn_header_title_if_not_exist),
            res.getString(R.string.home_nav_btn_header_title_if_done)
        )
    }

    val navigateButtonHeaderBodyText = _todayWorkoutSchedule.map {
        it.getResourcesByState(
            res.getString(R.string.home_nav_btn_header_body_if_exist),
            res.getString(R.string.home_nav_btn_header_body_if_not_exist),
            res.getString(R.string.home_nav_btn_header_body_if_done)
        )
    }

    val navigateButtonTitleText = _todayWorkoutSchedule.map {
        it.getResourcesByState(
            res.getString(R.string.home_nav_btn_title_text_if_exist),
            res.getString(R.string.home_nav_btn_title_text_if_not_exist),
            res.getString(R.string.home_nav_btn_title_text_if_done)
        )
    }

    val navigateButtonBodyText = _todayWorkoutSchedule.map {
        it.getResourcesByState(
            res.getString(R.string.home_nav_btn_body_text_if_exist),
            res.getString(R.string.home_nav_btn_body_text_if_not_exist),
            res.getString(R.string.home_nav_btn_body_text_if_done)
        )
    }

    val navigateButtonTailText = _todayWorkoutSchedule.map {
        it.getResourcesByState(
            res.getString(R.string.home_nav_btn_tail_text_if_exist),
            res.getString(R.string.home_nav_btn_tail_text_if_not_exist),
            res.getString(R.string.home_nav_btn_tail_text_if_done)
        )
    }

    val navigateButtonBackgroundImage = _todayWorkoutSchedule.map {
        it.getResourcesByState(
            ResourcesCompat.getDrawable(res, R.drawable.home_nav_btn_bg_img_if_exist, null),
            ResourcesCompat.getDrawable(res, R.drawable.home_nav_btn_bg_img_if_not_exist, null),
            ResourcesCompat.getDrawable(res, R.drawable.home_nav_btn_bg_img_if_done, null)
        )
    }

    fun onNavigateToScheduleDestButtonClicked() {
        viewModelScope.launch {
            _eventChannel.send(Event.NavigateToScheduleDest)
        }
    }

    private fun refreshVideoFromRepository() {
        viewModelScope.launch {
            try {
                repository.refreshVideos()
            } catch (e: IOException) {
                if (videos.value.isNullOrEmpty()) {
                    _eventChannel.send(Event.EventNetworkError)
                }
            }
        }
    }

    private fun <T> WorkoutSchedule?.getResourcesByState(
        existStateReturnVal: T,
        notExistStateReturnVal: T,
        doneStateReturnVal: T
    ): T {
        this?.let {
            return if (it.workoutDetails.isEmpty()) {
                notExistStateReturnVal
            } else {
                if (it.workoutOverview.trackingStatus == TrackingStatus.DONE) {
                    doneStateReturnVal
                } else {
                    existStateReturnVal
                }
            }
        } ?: return notExistStateReturnVal
    }

    // TODO : Real Implementation
    private fun createDummyData(): List<UserPersonalRecording> {
        val workoutList = res.getStringArray(R.array.exercise_list)
        val workoutSummaryInfoList = mutableListOf<UserPersonalRecording>()
        workoutList.forEach {
            workoutSummaryInfoList.add(
                UserPersonalRecording(it, 100.0, 1)
            )
        }
        return workoutSummaryInfoList.toList()
    }

}