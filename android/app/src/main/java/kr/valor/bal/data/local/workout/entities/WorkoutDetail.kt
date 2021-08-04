package kr.valor.bal.data.local.workout.entities

import androidx.room.*

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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "detail_id")
    val detailId: Long = 0L,

    @ColumnInfo(name = "container_id")
    var containerId: Long,

    @ColumnInfo(name = "workout_name")
    val workoutName: String

//    @ColumnInfo(name = "sets_detail")
//    val setsDetail: MutableList<WorkoutSet> = mutableListOf()
)



