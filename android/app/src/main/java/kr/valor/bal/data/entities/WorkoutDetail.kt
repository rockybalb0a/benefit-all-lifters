package kr.valor.bal.data.entities

import androidx.room.*
import kr.valor.bal.data.WorkoutSet
import java.util.*

@Entity(
    tableName = "workout_detail",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutOverview::class,
            parentColumns = ["overview_id"],
            childColumns = ["container_id"],
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("container_id")]
)
data class WorkoutDetail(
    @PrimaryKey
    @ColumnInfo(name = "detail_id")
    val detailId: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "container_id")
    val containerId: String,

    @ColumnInfo(name = "workout_name")
    val workoutName: String,

    @ColumnInfo(name = "sets_detail")
    val setsDetail: List<WorkoutSet>
)



