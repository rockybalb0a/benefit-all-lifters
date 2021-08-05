package kr.valor.bal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.toast.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
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

    override fun onPause() {
        super.onPause()
        viewModelStore.clear()
        overridePendingTransition(0, 0)
    }
}