package kr.valor.bal.utilities

import android.content.Context
import android.content.SharedPreferences
import kr.valor.bal.R
import java.time.LocalDate

class SharedPreferenceUtil(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            context.resources.getString(R.string.shared_preference_storage_name),
            Context.MODE_PRIVATE
        )

    private var keyPrefix: String = context.resources.getString(R.string.complete_state_key)

    fun getWorkoutRecordingState(): Boolean {
        val keySuffix = LocalDate.now().toString()
        return prefs.getBoolean(keyPrefix + keySuffix, false)
    }

    fun setWorkoutRecordingState(isCompleted: Boolean) {
        val keySuffix = LocalDate.now().toString()
        prefs.edit().putBoolean(keyPrefix + keySuffix, isCompleted).apply()
    }
}

