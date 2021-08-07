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

sealed class UserRecordData {
    abstract val id: String

    data class Content(val userPrRecord: UserPersonalRecording): UserRecordData() {
        override val id = userPrRecord.workoutName
    }
    object Footer: UserRecordData() {
        override val id = "Nothing"
    }

    companion object {

        fun convertToFooterAdapterList(userPrList: List<UserPersonalRecording>): List<UserRecordData> {
            val resultList = mutableListOf<UserRecordData>()
            val footerItem = Footer
            val contents = userPrList.map {
                Content(it)
            }
            resultList.addAll(contents)
            resultList.add(footerItem)

            return resultList.toList()
        }
    }
}