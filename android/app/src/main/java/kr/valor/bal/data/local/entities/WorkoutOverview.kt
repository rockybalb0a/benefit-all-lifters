package kr.valor.bal.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kr.valor.bal.utilities.TrackingStatus
import java.time.LocalDate

// TODO: Considering use of Domain Model
@Entity(tableName = "workout_overview")
data class WorkoutOverview(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "overview_id")
    val overviewId: Long = 0L,

    @ColumnInfo(name = "date")
    var date: LocalDate = LocalDate.now(),

    @ColumnInfo(name = "start_time_milli")
    var startTimeMilli: Long = 0L,

    @ColumnInfo(name = "end_time_milli")
    var endTimeMilli: Long = 0L,

    @ColumnInfo(name = "elapsed_time_milli")
    var elapsedTimeMilli: Long = 0L,

    @ColumnInfo(name = "tracking_time_milli")
    var trackingTimeMilli: Long = 0L,

    @ColumnInfo(name = "tracking_status")
    var trackingStatus: TrackingStatus = TrackingStatus.NONE
)