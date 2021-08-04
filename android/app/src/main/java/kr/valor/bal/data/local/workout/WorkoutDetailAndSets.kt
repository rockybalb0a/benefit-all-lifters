package kr.valor.bal.data.local.workout

import androidx.room.Embedded
import androidx.room.Relation
import kr.valor.bal.data.local.workout.entities.WorkoutDetail
import kr.valor.bal.data.local.workout.entities.WorkoutSet

data class WorkoutDetailAndSets(
    @Embedded val workoutDetail: WorkoutDetail,
    @Relation(
        parentColumn = "detail_id",
        entityColumn = "container_id"
    )
    val workoutSets: List<WorkoutSet>
)
