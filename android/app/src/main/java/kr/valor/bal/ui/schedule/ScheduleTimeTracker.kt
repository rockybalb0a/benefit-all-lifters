package kr.valor.bal.ui.schedule

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


object ScheduleTimeTracker {

    private const val TIME_UNIT = 1000L

    private const val ONE_SECOND = 1000L

    private val ticker = flow {
        while(true) {
            emit(TIME_UNIT)
            delay(ONE_SECOND)
        }
    }

    private var trackingJob: Job? = null

    val onTracking: Boolean
        get() = trackingJob != null

    fun activateTimeTracker(coroutineScope: CoroutineScope, block: (Long) -> Unit): Boolean {
        trackingJob = coroutineScope.launch {
            ticker.collectLatest {
                block(it)
            }
        }
        return true
    }

    fun deactivateTimeTracker(): Boolean {
        trackingJob?.cancel()
        trackingJob = null
        return false
    }

}