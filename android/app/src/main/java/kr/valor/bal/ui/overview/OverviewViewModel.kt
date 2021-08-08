package kr.valor.bal.ui.overview

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.utilities.TrackingStatus
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    repository: DefaultRepository
): ViewModel() {

    sealed class Event {
        data class NavigateToDetailDest(val overviewId: Long): Event()
    }

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow: Flow<Event>
        get() = _eventChannel.receiveAsFlow()

    val workoutSchedules = repository.workoutSchedules.map {
        it.filter { each ->
            each.workoutOverview.trackingStatus == TrackingStatus.DONE //&& each.workoutOverview.date != LocalDate.now()
        }
    }

    fun onOverviewItemClicked(overviewId: Long) {
        viewModelScope.launch {
            _eventChannel.send(Event.NavigateToDetailDest(overviewId))
        }
    }

}