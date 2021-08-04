package kr.valor.bal.data.local.workout.entities

import androidx.room.*

@Entity(
    tableName = "workout_set",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutDetail::class,
            parentColumns = ["detail_id"],
            childColumns = ["container_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("container_id")]
)
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "set_id")
    var setId: Long = 0L,

    @ColumnInfo(name = "container_id")
    val containerId: Long,

    @ColumnInfo
    var reps: Int = 0,

    @ColumnInfo
    var weights: Double = 20.0,

    @ColumnInfo(name = "inserted_plates_info")
    var platesStack: MutableList<Double> = mutableListOf()
)