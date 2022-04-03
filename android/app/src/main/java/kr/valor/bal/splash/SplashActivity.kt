package kr.valor.bal.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.valor.bal.MainActivity
import kr.valor.bal.onboarding.OnBoardingActivity

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.userInfoState.flowWithLifecycle(lifecycle)
                .collect { navigationDirection ->
                    when(navigationDirection) {
                        NavigationDirection.NavigateMain -> {
                            startActivity(
                                Intent(this@SplashActivity, MainActivity::class.java)
                            )
                        }

                        NavigationDirection.NavigateOnboarding -> {
                            startActivity(
                                Intent(this@SplashActivity, OnBoardingActivity::class.java)
                            )
                        }
                    }
                    finish()
                }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModelStore.clear()
        overridePendingTransition(0, 0)
    }
}