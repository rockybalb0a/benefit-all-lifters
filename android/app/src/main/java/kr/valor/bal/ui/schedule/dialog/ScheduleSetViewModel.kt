package kr.valor.bal.ui.schedule.dialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.valor.bal.data.WorkoutDao
import javax.inject.Inject

@HiltViewModel
class ScheduleSetViewModel @Inject constructor(workoutDao: WorkoutDao) : ViewModel() {

}