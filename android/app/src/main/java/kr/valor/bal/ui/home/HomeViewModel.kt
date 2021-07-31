package kr.valor.bal.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutOverview
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

    private val _todayWorkoutOverview = workoutRepo.getWorkoutOverviewOfToday()

    private val _todayWorkoutSchedule = _todayWorkoutOverview.switchMap {
        it?.let { workoutRepo.getWorkoutScheduleByWorkoutOverviewId(it.overviewId) } ?: liveData { emit(null) }
    }
    val todayWorkoutSchedule: LiveData<out WorkoutSchedule?>
        get() = _todayWorkoutSchedule

    val navigateButtonVisibility = _todayWorkoutSchedule.map {
        it?.workoutDetails?.isEmpty() ?: false
    }

    fun onNavigateToScheduleDestButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToScheduleDest)
        }
    }

}