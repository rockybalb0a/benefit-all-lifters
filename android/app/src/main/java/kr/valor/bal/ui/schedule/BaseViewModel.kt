package kr.valor.bal.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.local.workout.entities.WorkoutDetail
import kr.valor.bal.data.local.workout.entities.WorkoutOverview
import kr.valor.bal.data.local.workout.entities.WorkoutSet

abstract class BaseViewModel (private val repo: DefaultRepository): ViewModel(){

    abstract fun onAddNewWorkoutButtonClicked()

    abstract fun onDialogItemSelected(newWorkoutName: String)

    fun onAddNewSetButtonClicked(detailId: Long) {
        val newWorkoutSet = WorkoutSet(containerId = detailId)
        insertWorkoutSet(newWorkoutSet)
    }

    fun onDeleteSetButtonClicked(detailId: Long) {
        deleteWorkoutSet(detailId)
    }

    fun onCloseButtonClicked(workoutDetail: WorkoutDetail) {
        deleteWorkoutDetail(workoutDetail)
    }

    private fun insertWorkoutSet(workoutSet: WorkoutSet) {
        viewModelScope.launch {
            repo.getLatestWorkoutSetInfo(workoutSet)?.let { latestWorkoutSet ->
                workoutSet.weights = latestWorkoutSet.weights
                workoutSet.reps = latestWorkoutSet.reps
                workoutSet.platesStack = latestWorkoutSet.platesStack
            }
            repo.insertWorkoutSet(workoutSet)
        }
    }

    private fun deleteWorkoutSet(detailId: Long) {
        viewModelScope.launch {
            repo.removeWorkoutSet(detailId)
        }
    }

    protected fun insertWorkoutDetail(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            repo.addWorkoutDetail(workoutDetail)
        }
    }


    private fun deleteWorkoutDetail(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            repo.dropWorkoutDetail(workoutDetail)
        }
    }

    protected fun updateWorkoutOverview(workoutOverview: WorkoutOverview) {
        viewModelScope.launch {
            repo.updateWorkoutOverview(workoutOverview)
        }
    }
}