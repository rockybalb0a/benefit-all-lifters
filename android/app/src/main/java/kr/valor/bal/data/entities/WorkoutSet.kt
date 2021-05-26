package kr.valor.bal.data.entities

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
    val setId: Long = 0L,

    @ColumnInfo(name = "container_id")
    val containerId: Long,

    @ColumnInfo
    var reps: Byte = 0,

    @ColumnInfo
    var weights: Short = 0,
)