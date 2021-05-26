package kr.valor.bal.data

import androidx.room.Embedded
import androidx.room.Relation
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview

data class WorkoutSchedule(
    @Embedded val workoutOverview: WorkoutOverview,
    @Relation(
        entity = WorkoutDetail::class,
        parentColumn = "overview_id",
        entityColumn = "container_id"
    )
    val workoutDetails: List<WorkoutDetailAndSets>
)
