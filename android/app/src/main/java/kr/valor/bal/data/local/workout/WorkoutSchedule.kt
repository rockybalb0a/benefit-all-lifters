package kr.valor.bal.data.local.workout

import androidx.room.Embedded
import androidx.room.Relation
import kr.valor.bal.data.local.workout.entities.WorkoutDetail
import kr.valor.bal.data.local.workout.entities.WorkoutOverview

data class WorkoutSchedule(
    @Embedded val workoutOverview: WorkoutOverview,
    @Relation(
        entity = WorkoutDetail::class,
        parentColumn = "overview_id",
        entityColumn = "container_id"
    )
    val workoutDetails: List<WorkoutDetailAndSets>
)
