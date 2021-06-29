package kr.valor.bal.ui.schedule

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kr.valor.bal.data.WorkoutDao
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.utilities.TrackingStatus
import java.time.LocalDate
import javax.inject.Inject
import kotlin.Long as Long

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val workoutDao: WorkoutDao
) : ViewModel() {

    private val _currentWorkoutOverview = liveData {
        val currentDate = LocalDate.now()
        val currentWorkoutOverview = workoutDao.getWorkoutOverviewByDate(currentDate)
            ?: run {
                val newWorkoutOverview = WorkoutOverview()
                workoutDao.insert(newWorkoutOverview)
                workoutDao.getLatestWorkoutOverview()
            }
        syncElapsedTimeWithDatabase(currentWorkoutOverview)
        emit(currentWorkoutOverview)
    }


    private val _currentWorkoutSchedule = _currentWorkoutOverview.switchMap {
        workoutDao.getWorkoutSchedule(it.overviewId)
    }

    val currentWorkoutSchedule: LiveData<WorkoutSchedule>
        get() = _currentWorkoutSchedule

    private val _trackingJob = MutableLiveData<Job?>()

    val onTracking = _trackingJob.map {
        it != null
    }

    private val _elapsedTimeMilli = MutableLiveData<Long>()

    val elapsedTimeMilli: LiveData<Long>
        get() = _elapsedTimeMilli


    val layoutViewVisibility = _currentWorkoutSchedule.map {
        it.workoutDetails.isNotEmpty()
    }

    private val timer = flow {
        while (true) {
            emit(1000L)
            delay(1000L)
        }
    }


    private fun activateTimer() {
        _trackingJob.value = viewModelScope.launch {
            timer.collect { tick ->
                _currentWorkoutOverview.value?.let {
                    it.elapsedTimeMilli += tick
                    _elapsedTimeMilli.value = it.elapsedTimeMilli
                    updateWorkoutOverview(it)
                }
            }
        }
    }

    private fun deactivateTimer() {
        _trackingJob.value?.cancel()
        _trackingJob.value = null
    }

    private fun syncElapsedTimeWithDatabase(currentWorkoutOverview: WorkoutOverview) {
        if (currentWorkoutOverview.trackingStatus == TrackingStatus.TRACKING) {
            with(currentWorkoutOverview) {
                elapsedTimeMilli = System.currentTimeMillis() - startTimeMilli
            }
            _trackingJob.value?.let { deactivateTimer() } ?: activateTimer()
        }
        _elapsedTimeMilli.value = currentWorkoutOverview.elapsedTimeMilli
    }

    private fun onStart() {
        _currentWorkoutOverview.value?.let {
            it.trackingStatus = TrackingStatus.TRACKING
            it.startTimeMilli = System.currentTimeMillis()
            updateWorkoutOverview(it)
        }
        activateTimer()
    }

    private fun onPause() {
        deactivateTimer()
        _currentWorkoutOverview.value?.let {
            it.trackingStatus = TrackingStatus.PAUSE
            updateWorkoutOverview(it)
        }
    }

    private fun onResume() {
        _currentWorkoutOverview.value?.let {
            if (it.trackingStatus == TrackingStatus.PAUSE)  {
                it.trackingStatus = TrackingStatus.TRACKING
                updateWorkoutOverview(it)
            }
        }
        activateTimer()
    }


    private fun onStop() {
        deactivateTimer()
        _elapsedTimeMilli.value = 0L
        _currentWorkoutOverview.value?.let {
            it.trackingStatus = TrackingStatus.DONE
            it.endTimeMilli  = it.startTimeMilli + it.elapsedTimeMilli
            it.elapsedTimeMilli = 0L
            updateWorkoutOverview(it)
        }
    }


    fun toggleTimer() {
        val trackingStatus = _currentWorkoutOverview.value!!.trackingStatus
        if (trackingStatus == TrackingStatus.NONE) {
            onStart()
            return
        }
        _trackingJob.value?.let { onPause() } ?: onResume()
    }

    fun stopTimer() {
        onStop()
    }

    // For testing
    fun clearTimer() {
        _currentWorkoutOverview.value?.let {
            with(it) {
                startTimeMilli = 0L
                elapsedTimeMilli = 0L
                endTimeMilli = 0L
                trackingStatus = TrackingStatus.NONE
            }
            updateWorkoutOverview(it)
        }
    }

    fun onWorkoutFinished() {
        onStop()
    }

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

    fun onDialogItemSelected(newWorkoutName: String) {
        _currentWorkoutOverview.value?.let {
            val newWorkoutDetail = WorkoutDetail(
                containerId = it.overviewId,
                workoutName = newWorkoutName
            )
            insertWorkoutDetail(newWorkoutDetail)
        }
    }

    private fun insertWorkoutSet(workoutSet: WorkoutSet) {
        viewModelScope.launch {
            val latestWorkoutSet = workoutDao.getLatestWorkoutSetByWorkoutDetailId(workoutSet.containerId)
            latestWorkoutSet?.let {
                workoutSet.weights = it.weights
                workoutSet.reps = it.reps
                workoutSet.platesStack = it.platesStack
            }
            workoutDao.insert(workoutSet)
        }
    }


    private fun deleteWorkoutSet(detailId: Long) {
        viewModelScope.launch {
            workoutDao.deleteWorkoutSetAssociatedWithWorkoutDetail(detailId)
        }
    }

    private fun insertWorkoutDetail(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            workoutDao.insert(workoutDetail)
        }
    }

    private fun deleteWorkoutDetail(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            workoutDao.delete(workoutDetail)
        }
    }

    private fun updateWorkoutOverview(workoutOverview: WorkoutOverview) {
        viewModelScope.launch {
            workoutDao.update(workoutOverview)
        }
    }

}

