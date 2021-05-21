package kr.valor.bal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

    private val _newWorkoutClickedEvent = MutableLiveData<Boolean?>()
    val newWorkoutClickedEvent: LiveData<Boolean?>
        get() = _newWorkoutClickedEvent

    private val _restTodayClickedEvent = MutableLiveData<Boolean?>()
    val restTodayClickedEvent: LiveData<Boolean?>
        get() = _restTodayClickedEvent

    fun onNewWorkoutClicked() {
        _newWorkoutClickedEvent.value = true
    }

    fun doneNewWorkoutClicked() {
        _newWorkoutClickedEvent.value = null
    }

    fun onRestTodayClickedEvent() {
        _restTodayClickedEvent.value = true
    }

    fun doneRestTodayClickedEvent() {
        _restTodayClickedEvent.value = null
    }
}