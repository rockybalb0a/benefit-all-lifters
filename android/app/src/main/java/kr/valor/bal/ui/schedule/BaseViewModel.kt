package kr.valor.bal.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.local.entities.WorkoutDetail
import kr.valor.bal.data.local.entities.WorkoutOverview
import kr.valor.bal.data.local.entities.WorkoutSet
import javax.inject.Inject

abstract class BaseViewModel (private val workoutRepo: DefaultRepository): ViewModel(){

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
            workoutRepo.getLatestWorkoutSetInfo(workoutSet)?.let { latestWorkoutSet ->
                workoutSet.weights = latestWorkoutSet.weights
                workoutSet.reps = latestWorkoutSet.reps
                workoutSet.platesStack = latestWorkoutSet.platesStack
            }
            workoutRepo.insertWorkoutSet(workoutSet)
        }
    }

    private fun deleteWorkoutSet(detailId: Long) {
        viewModelScope.launch {
            workoutRepo.removeWorkoutSet(detailId)
        }
    }

    protected fun insertWorkoutDetail(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            workoutRepo.addWorkoutDetail(workoutDetail)
        }
    }


    private fun deleteWorkoutDetail(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            workoutRepo.dropWorkoutDetail(workoutDetail)
        }
    }

    protected fun updateWorkoutOverview(workoutOverview: WorkoutOverview) {
        viewModelScope.launch {
            workoutRepo.updateWorkoutOverview(workoutOverview)
        }
    }
}