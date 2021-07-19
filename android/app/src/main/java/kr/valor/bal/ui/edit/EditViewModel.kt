package kr.valor.bal.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.WorkoutSchedule
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(private val workoutRepo: DefaultRepository, savedStateHandle: SavedStateHandle): ViewModel() {
    private val _overviewId: Long = savedStateHandle["overviewId"]!!

    private val _workoutSchedule = liveData<WorkoutSchedule> {
        emitSource(workoutRepo.getWorkoutScheduleByWorkoutOverviewId(_overviewId))
    }
    val workoutSchedule: LiveData<WorkoutSchedule>
        get() = _workoutSchedule

}