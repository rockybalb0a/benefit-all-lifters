package kr.valor.bal.ui.overview.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.valor.bal.data.WorkoutDao
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.WorkoutSchedule
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val workoutDao: WorkoutDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val overviewId: Long? = savedStateHandle["overviewId"]

    private val _workoutSchedule = liveData {
        overviewId?.let { id ->
            emitSource(workoutDao.getWorkoutSchedule(id))
        }
    }
    val workoutSchedule: LiveData<WorkoutSchedule>
        get() = _workoutSchedule

}