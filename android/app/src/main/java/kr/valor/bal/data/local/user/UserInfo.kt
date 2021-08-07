package kr.valor.bal.data.local.user

import androidx.room.*
import java.time.LocalDate
import java.util.*

@Entity(tableName = "user_info")
data class UserInfo(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "beginning_date")
    var beginningOfWorkout: LocalDate
)

@Entity(
    tableName = "user_pr_record",
    foreignKeys = [
        ForeignKey(
            entity = UserInfo::class,
            parentColumns = ["user_id"],
            childColumns = ["user_key"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_key")]
)
data class UserPersonalRecording(
    @PrimaryKey
    @ColumnInfo(name = "workout_name")
    val workoutName: String,

    @ColumnInfo(name = "user_key")
    val userKey: String,

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

        fun convertToFooterAdapterList(userPrList: List<UserPersonalRecording>): List<UserRecordData> =
            generateBaseList(userPrList).toList()

        private fun generateBaseList(userPrList: List<UserPersonalRecording>): MutableList<UserRecordData> {
            val contents = userPrList.map {
                Content(it)
            }
            val footerItem = Footer
            val newList = mutableListOf<UserRecordData>()
            newList.addAll(contents)
            newList.add(footerItem)

            return newList
        }
    }
}