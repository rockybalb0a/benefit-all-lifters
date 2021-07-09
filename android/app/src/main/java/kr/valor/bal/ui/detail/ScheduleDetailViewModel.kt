package kr.valor.bal.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.WorkoutSchedule
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val workoutRepo: DefaultRepository
) : ViewModel() {

    private val overviewId: Long? = savedStateHandle["overviewId"]

    private val _workoutSchedule = liveData {
        overviewId?.let {
            emitSource(workoutRepo.getWorkoutScheduleByWorkoutOverviewId(overviewId))
        }
    }
    val workoutSchedule: LiveData<WorkoutSchedule>
        get() = _workoutSchedule
}