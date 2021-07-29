package kr.valor.bal.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.WorkoutSchedule
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val workoutRepo: DefaultRepository
): ViewModel() {

    sealed class Event {
        object NavigateToScheduleDest: Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _todayWorkoutSchedule =
        workoutRepo.getWorkoutOverviewOfToday(null).switchMap {
            workoutRepo.getWorkoutScheduleByWorkoutOverviewId(it.overviewId)
        }
    val todayWorkoutSchedule: LiveData<WorkoutSchedule>
        get() = _todayWorkoutSchedule

    fun onWorkoutStartButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToScheduleDest)
        }
    }

}