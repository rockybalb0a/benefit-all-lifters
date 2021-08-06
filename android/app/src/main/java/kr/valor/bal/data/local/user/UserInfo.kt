package kr.valor.bal.data.local.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*

@Entity(tableName = "user_info")
data class UserInfo(
    @PrimaryKey
    val userId: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "beginning_date")
    var beginningOfWorkout: LocalDate,

    @ColumnInfo(name = "user_pr_list")
    val userPRList: MutableList<UserPersonalRecording> = mutableListOf()
)

data class UserPersonalRecording(
    val workoutName: String,
    var weights: Double = 0.0,
    var reps: Int = 0
)