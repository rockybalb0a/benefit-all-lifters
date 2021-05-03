package kr.valor.bal.data.source.entities

import androidx.room.*
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "workout_overview")
data class WorkoutOverview(
        @PrimaryKey
        @ColumnInfo(name = "overview_id")
        val overviewId: String = UUID.randomUUID().toString(),

        @ColumnInfo(name = "date")
        var date: LocalDate = LocalDate.now(),

        @ColumnInfo(name = "elapsed_time")
        var elapsedTime: Long = 0L
)

@Entity(
        tableName = "workout_detail",
        foreignKeys = [
                ForeignKey(entity = WorkoutOverview::class, parentColumns = ["overview_id"], childColumns = ["super_id"], onDelete = ForeignKey.CASCADE)
        ]
)
data class WorkoutDetail(
        @PrimaryKey
        @ColumnInfo(name = "detail_id")
        val detailId: String = UUID.randomUUID().toString(),

        @ColumnInfo(name = "super_id")
        val superId: String,

        @ColumnInfo(name = "workout_name")
        val workoutName: String,

        @ColumnInfo(name = "sets_detail")
        val setsDetail: List<WorkoutSet>
)

@Serializable
data class WorkoutSet(
        val weight: Short,
        val reps: Byte
)

data class DailyRoutine(
        @Embedded val workoutOverview: WorkoutOverview,
        @Relation(
                parentColumn = "overview_id",
                entityColumn = "super_id"
        )
        val workoutDetails: List<WorkoutDetail>
)






