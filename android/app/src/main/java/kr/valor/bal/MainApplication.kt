package kr.valor.bal

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kr.valor.bal.utilities.SharedPreferenceUtil

@HiltAndroidApp
class MainApplication : Application() {

    companion object {
        lateinit var prefs: SharedPreferenceUtil
    }

    override fun onCreate() {
        prefs = SharedPreferenceUtil(this)
        super.onCreate()
    }
}