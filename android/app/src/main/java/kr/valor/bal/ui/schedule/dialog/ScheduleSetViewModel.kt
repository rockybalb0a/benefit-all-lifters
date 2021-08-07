package kr.valor.bal.ui.schedule.dialog

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.local.workout.entities.WorkoutSet
import javax.inject.Inject

@HiltViewModel
class ScheduleSetViewModel @Inject constructor(
    private val repository: DefaultRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val setId: Long? = savedStateHandle["setId"]

    private val _currentWorkoutSet = liveData{
        setId?.let {
            emitSource(repository.getWorkoutSetById(it))
        }
    }
    val currentWorkoutSet: LiveData<WorkoutSet>
        get() = _currentWorkoutSet


    private val _candidatePlatesLiveData = MutableLiveData<Double?>()
    val candidatePlatesLiveData: LiveData<Double?>
        get() = _candidatePlatesLiveData

    val popButtonEnabled: LiveData<Boolean> = _currentWorkoutSet.map {
        it.platesStack.isNotEmpty()
    }

    val checkedPlatesTypeBtnIndex = MutableLiveData(PLATES_TYPE_DEFAULT)

    val checkedRepsUnitBtnIndex = MutableLiveData(REPS_UNIT_SINGLE)

    fun applyChange() {
        _currentWorkoutSet.value?.let { workoutSet ->
            _candidatePlatesLiveData.value?.let { weight ->
                workoutSet.platesStack.add(weight)
                workoutSet.weights += calculateTotalWeights(weight)
                update(workoutSet)
            }
        }
    }

    fun discardChange() {
        dequeuePlates()
    }


    fun insertPlates(weight: Double) {
        when (checkedPlatesTypeBtnIndex.value) {
            PLATES_TYPE_SMALL -> weight / 10
            else -> weight
        }.also { convertedWeight ->
            enqueuePlates(convertedWeight)
        }
    }

    fun popPlates() {
        _currentWorkoutSet.value?.let {
            if (it.platesStack.isNotEmpty()) {
                val poppedPlatesWeight = it.platesStack.removeLast()
                it.weights -= calculateTotalWeights(poppedPlatesWeight)
            }
            update(it)
        }
    }

    fun plusReps() {
        _currentWorkoutSet.value?.let { workoutSet ->
            workoutSet.reps += calculateRepsWithUserSelection()
            update(workoutSet)
        }
    }

    fun minusReps() {
        _currentWorkoutSet.value?.let { workoutSet ->
            val difference = workoutSet.reps - calculateRepsWithUserSelection()
            workoutSet.reps = if (difference >= 0) difference else 0
            update(workoutSet)
        }
    }


    private fun update(workoutSet: WorkoutSet) {
        viewModelScope.launch {
            repository.updateWorkoutSet(workoutSet)
            adjustUserPR(workoutSet)
            _candidatePlatesLiveData.value?.let { dequeuePlates() }
        }
    }

    private suspend fun adjustUserPR(workoutSet: WorkoutSet) {
        val workoutName = repository.getWorkoutNameByWorkoutSetId(workoutSet.setId)
        val userRecordOfThisWorkout = repository.getPrInfoOfThisWorkout(workoutName)

        // Weight PR
        if (userRecordOfThisWorkout.weights < workoutSet.weights && workoutSet.reps > 0) {
            userRecordOfThisWorkout.weights = workoutSet.weights
            userRecordOfThisWorkout.reps = workoutSet.reps
            repository.updateUserPersonalRecording(userRecordOfThisWorkout)
            return
        }

        // Reps PR
        if (userRecordOfThisWorkout.weights == workoutSet.weights && userRecordOfThisWorkout.reps < workoutSet.reps) {
            userRecordOfThisWorkout.reps = workoutSet.reps
            repository.updateUserPersonalRecording(userRecordOfThisWorkout)
            return
        }

    }

    private fun enqueuePlates(weight: Double) {
        _candidatePlatesLiveData.value = weight
    }

    private fun dequeuePlates() {
        _candidatePlatesLiveData.value = null
    }

    private fun calculateTotalWeights(weight: Double): Double = weight * 2

    private fun calculateRepsWithUserSelection(): Int {
        return when(checkedRepsUnitBtnIndex.value) {
            REPS_UNIT_FIVE -> 5
            REPS_UNIT_TEN -> 10
            else -> 1
        }
    }

    companion object {
        private const val PLATES_TYPE_DEFAULT = 0
        private const val PLATES_TYPE_SMALL = 1

        private const val REPS_UNIT_SINGLE = 0
        private const val REPS_UNIT_FIVE = 1
        private const val REPS_UNIT_TEN = 2
    }

}