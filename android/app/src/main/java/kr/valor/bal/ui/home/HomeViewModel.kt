package kr.valor.bal.ui.home

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.valor.bal.R
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.utilities.TrackingStatus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val workoutRepo: DefaultRepository
): AndroidViewModel(application) {

    sealed class Event {
        object NavigateToScheduleDest: Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _todayWorkoutOverview = workoutRepo.getWorkoutOverviewOfToday()

    private val _todayWorkoutSchedule = _todayWorkoutOverview.switchMap {
        it?.let { workoutRepo.getWorkoutScheduleByWorkoutOverviewId(it.overviewId) } ?: liveData { emit(null) }
    }
    val todayWorkoutSchedule: LiveData<out WorkoutSchedule?>
        get() = _todayWorkoutSchedule


    val navigateButtonVisibility = _todayWorkoutSchedule.map {
        it?.workoutDetails?.isEmpty() ?: false
    }

    val navigateButtonTitleText = _todayWorkoutSchedule.map {
        it?.let {
            if (it.workoutDetails.isEmpty()) {
                application.resources.getString(R.string.home_nav_btn_title_text_if_not_exist)
            } else {
                if (it.workoutOverview.trackingStatus == TrackingStatus.DONE) {
                    application.resources.getString(R.string.home_nav_btn_title_text_if_done)
                } else {
                    application.resources.getString(R.string.home_nav_btn_title_text_if_exist)
                }
            }
        } ?: application.resources.getString(R.string.home_nav_btn_title_text_if_not_exist)
    }

    val navigateButtonSubTitleText = _todayWorkoutSchedule.map {
        it?.let {
            if (it.workoutDetails.isEmpty()) {
                application.resources.getString(R.string.home_nav_btn_sub_title_text_if_not_exist)
            } else {
                if (it.workoutOverview.trackingStatus == TrackingStatus.DONE) {
                    application.resources.getString(R.string.home_nav_btn_sub_title_if_done)
                } else {
                    application.resources.getString(R.string.home_nav_btn_sub_title_text_if_exist)
                }
            }
        } ?: application.resources.getString(R.string.home_nav_btn_sub_title_text_if_not_exist)
    }

    val navigateButtonBackgroundImage = _todayWorkoutSchedule.map {
        it?.let {
            if (it.workoutDetails.isEmpty()) {
                ResourcesCompat.getDrawable(application.resources, R.drawable.home_nav_btn_on_record_or_not_exist_bg_img, null)
            } else {
                if (it.workoutOverview.trackingStatus == TrackingStatus.DONE) {
                    ResourcesCompat.getDrawable(application.resources, R.drawable.home_nav_btn_on_done_bg_img, null)
                } else {
                    ResourcesCompat.getDrawable(application.resources, R.drawable.background_image_press, null)
                }
            }
        } ?: ResourcesCompat.getDrawable(application.resources, R.drawable.home_nav_btn_on_record_or_not_exist_bg_img, null)!!
    }



    fun onNavigateToScheduleDestButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToScheduleDest)
        }
    }

}