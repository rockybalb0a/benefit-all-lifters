package kr.valor.bal.data

import androidx.room.Embedded
import androidx.room.Relation
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutSet

data class WorkoutDetailAndSets(
    @Embedded val workoutDetail: WorkoutDetail,
    @Relation(
        parentColumn = "detail_id",
        entityColumn = "container_id"
    )
    val workoutSets: List<WorkoutSet>
)
