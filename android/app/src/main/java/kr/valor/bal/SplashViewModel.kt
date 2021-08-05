package kr.valor.bal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.local.user.UserInfo
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(repository: DefaultRepository): ViewModel() {

    private val _userInfo = repository.userInfo
    val userInfo: LiveData<UserInfo?>
        get() = _userInfo

}