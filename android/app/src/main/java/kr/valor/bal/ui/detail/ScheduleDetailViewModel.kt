package kr.valor.bal.ui.detail

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.local.WorkoutSchedule
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val workoutRepo: DefaultRepository
) : ViewModel() {

    sealed class Event {
        object NavigateToOverviewDest: Event()
        data class NavigateToEditDest(val overviewId: Long, val date: String): Event()
    }

    private val overviewId: Long = savedStateHandle["overviewId"]!!

    private val _workoutSchedule = liveData {
        emitSource(workoutRepo.getWorkoutScheduleByWorkoutOverviewId(overviewId))
    }
    val workoutSchedule: LiveData<WorkoutSchedule>
        get() = _workoutSchedule

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventFlow = eventChannel.receiveAsFlow()

    fun onBackButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToOverviewDest)
        }
    }

    fun onEditButtonClicked() {
        viewModelScope.launch {
            val date = workoutSchedule.value!!.workoutOverview.date.toString()
            eventChannel.send(Event.NavigateToEditDest(overviewId, date))
        }
    }
}