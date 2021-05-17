package kr.valor.bal.data

import androidx.room.*
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview

data class WorkoutOverviewAndDetails(
        @Embedded val workoutOverview: WorkoutOverview,
        @Relation(
                parentColumn = "overview_id",
                entityColumn = "container_id"
        )
        val workoutDetails: List<WorkoutDetail>
)






