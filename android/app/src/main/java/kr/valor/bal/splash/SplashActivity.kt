package kr.valor.bal.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.onboarding.OnBoardingActivity

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
            startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
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