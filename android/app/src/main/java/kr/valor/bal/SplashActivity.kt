package kr.valor.bal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kr.valor.bal.utilities.observeInLifecycle

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
            lifecycleScope.launch {
                delay(1000L)
                NavDeepLinkBuilder(this@SplashActivity).apply {
                    setComponentName(MainActivity::class.java)
                    setGraph(R.navigation.nav_graph)
                    setDestination(R.id.home_dest)
                    createPendingIntent().send()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModelStore.clear()
        overridePendingTransition(0, 0)
    }
}