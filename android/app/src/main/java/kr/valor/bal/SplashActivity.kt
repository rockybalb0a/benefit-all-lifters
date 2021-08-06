package kr.valor.bal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.local.user.UserInfo
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.userInfo.observe(this) {
            if (it == null) {
                Toast.makeText(this, "Value is null", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Value is not null", Toast.LENGTH_SHORT).show()
            }
            startActivity(Intent(this@SplashActivity, SettingsActivity::class.java))
            finish()
//            lifecycleScope.launch {
//                delay(1000L)
//                NavDeepLinkBuilder(this@SplashActivity).apply {
//                    setComponentName(MainActivity::class.java)
//                    setGraph(R.navigation.nav_graph)
//                    setDestination(R.id.home_dest)
//                    createPendingIntent().send()
//                }
//            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModelStore.clear()
        overridePendingTransition(0, 0)
    }
}

@HiltViewModel
private class SplashViewModel @Inject constructor(repository: DefaultRepository): ViewModel() {

    private val _userInfo = repository.userInfo
    val userInfo: LiveData<UserInfo?>
        get() = _userInfo

}