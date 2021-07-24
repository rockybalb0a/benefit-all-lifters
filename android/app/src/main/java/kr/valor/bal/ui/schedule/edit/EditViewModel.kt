package kr.valor.bal.ui.schedule.edit

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.ui.schedule.BaseViewModel
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class EditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val workoutRepo: DefaultRepository
): BaseViewModel(workoutRepo) {

    sealed class Event {
        object ShowAddNewWorkoutDialog: Event()
        object ShowTimerSettingDialog: Event()
        data class EditDoneAndBackToDetailDest(val overviewId: Long): Event()
        data class EditRejectAndBackToDetailDest(val overviewId: Long): Event()
    }

    private val overviewId: Long = savedStateHandle["overviewId"]!!

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _currentWorkoutOverview = workoutRepo.getWorkoutOverviewCachedById(overviewId)

    private var _currentWorkoutSchedule = _currentWorkoutOverview.switchMap {
        workoutRepo.getWorkoutScheduleCachedByWorkoutOverviewId(it.overviewId)
    }

    val currentWorkoutSchedule: LiveData<WorkoutSchedule>
        get() = _currentWorkoutSchedule

    private val _recyclerViewVisibility = MutableLiveData(true)
    val recyclerViewVisibility: LiveData<Boolean>
        get() = _recyclerViewVisibility

    fun onEditWorkoutTimerSettingButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.ShowTimerSettingDialog)
        }
    }

    fun onEditTimerButtonClicked(minutes: Long) {
        _currentWorkoutOverview.value!!.let {
            it.elapsedTimeMilli += minutes * 60000L
            updateWorkoutOverview(it)
        }
        Log.d("update-before", "${_currentWorkoutSchedule.value!!.workoutOverview.elapsedTimeMilli}, ${workoutRepo.workoutScheduleCached.value!!.workoutOverview.elapsedTimeMilli}")
    }

    fun onEditFinishButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.EditDoneAndBackToDetailDest(overviewId))
        }
    }

    fun onEditRejectButtonClicked() {
        viewModelScope.launch {
            _recyclerViewVisibility.value = false
            if(workoutRepo.restore(overviewId, viewModelScope)) {
                eventChannel.send(Event.EditRejectAndBackToDetailDest(overviewId))
            }
        }
    }


    override fun onAddNewWorkoutButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.ShowAddNewWorkoutDialog)
        }
    }


    override fun onDialogItemSelected(newWorkoutName: String) {
        _currentWorkoutOverview.value?.let {
            val newWorkoutDetail = WorkoutDetail(
                containerId = it.overviewId,
                workoutName = newWorkoutName
            )
            insertWorkoutDetail(newWorkoutDetail)
        }
    }

}