package kr.valor.bal.utilities

import android.content.Context
import android.content.SharedPreferences
import kr.valor.bal.R
import java.time.LocalDate

class SharedPreferenceUtil(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            context.resources.getString(R.string.shared_preference_storage_name),
            Context.MODE_PRIVATE
        )

    private var key: String = context.resources.getString(R.string.complete_state_key) +
            LocalDate.now().toString()

    init {
        // TODO : Clear Before Status
    }

    fun getWorkoutRecordingState(): Boolean {
        return prefs.getBoolean(key, false)
    }

    fun setWorkoutRecordingState(isCompleted: Boolean) {
        prefs.edit().putBoolean(key, isCompleted).apply()
    }
}

