package kr.valor.bal.utilities

import kr.valor.bal.data.entities.WorkoutOverview
import java.time.LocalDate
import kotlin.random.Random

fun randomGenerator(): List<WorkoutOverview> {
    val list = mutableListOf<WorkoutOverview>()
    for (i in 0..10) {
        val elem = WorkoutOverview(
            overviewId = i.toString(),
            date = LocalDate.now().minusDays(i.toLong()),
            startTimeMilli = Random.nextLong(3000_000L, 4000_000L),
            endTimeMilli = Random.nextLong(8000_000L, 10_000_000L)
        )
        list.add(elem)
    }
    return list
}