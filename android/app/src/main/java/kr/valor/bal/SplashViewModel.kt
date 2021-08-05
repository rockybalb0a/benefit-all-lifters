package kr.valor.bal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(): ViewModel() {
    private val _toast = MutableLiveData<Long>()
    val toast: LiveData<Long>
        get() = _toast

    init {
        _toast.value = 10L
    }
}