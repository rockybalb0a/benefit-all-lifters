package kr.valor.bal.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kr.valor.bal.data.DefaultRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(repository: DefaultRepository) : ViewModel() {

    val userInfoState = repository.userInfo.asFlow().map {
        it?.let {
            NavigationDirection.NavigateMain
        } ?: NavigationDirection.NavigateOnboarding
    }

}

sealed class NavigationDirection {
    object NavigateMain : NavigationDirection()
    object NavigateOnboarding : NavigationDirection()
}