package kr.valor.bal.ui.schedule.dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.valor.bal.data.WorkoutDao
import javax.inject.Inject

@HiltViewModel
class ScheduleSetViewModel @Inject constructor(workoutDao: WorkoutDao) : ViewModel() {

    val checkedPlatesTypeBtnIndex = MutableLiveData(PLATES_TYPE_SMALL)

    val checkedRepsUnitBtnIndex = MutableLiveData(REPS_UNIT_TEN)



    companion object {
        private const val PLATES_TYPE_DEFAULT = 0
        private const val PLATES_TYPE_SMALL = 1

        private const val REPS_UNIT_SINGLE = 0
        private const val REPS_UNIT_FIVE = 1
        private const val REPS_UNIT_TEN = 2

    }

}