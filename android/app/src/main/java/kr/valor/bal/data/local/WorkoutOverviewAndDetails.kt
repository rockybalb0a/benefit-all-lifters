package kr.valor.bal.data.local

import androidx.room.*
import kr.valor.bal.data.local.entities.WorkoutDetail
import kr.valor.bal.data.local.entities.WorkoutOverview

data class WorkoutOverviewAndDetails(
    @Embedded val workoutOverview: WorkoutOverview,
    @Relation(
                parentColumn = "overview_id",
                entityColumn = "container_id"
        )
        val workoutDetails: List<WorkoutDetail>
)






