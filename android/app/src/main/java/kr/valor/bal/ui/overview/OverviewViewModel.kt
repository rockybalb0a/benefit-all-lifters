package kr.valor.bal.ui.overview

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.WorkoutDao
import kr.valor.bal.utilities.TrackingStatus
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    workoutRepo: DefaultRepository
): ViewModel() {

    val workoutSchedules = workoutRepo.getAllWorkoutSchedule().map {
        it.filter { each ->
            each.workoutOverview.trackingStatus == TrackingStatus.DONE
        }
    }

}