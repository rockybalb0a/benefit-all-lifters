package kr.valor.bal.data.local.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insertUserInfo(item: UserInfo)

    @Query("SELECT * FROM user_info LIMIT 1")
    fun getUserInfo(): LiveData<UserInfo?>

    @Insert
    suspend fun insertUserPrRecording(item: UserPersonalRecording)

    @Update
    suspend fun updateUserPrRecording(item: UserPersonalRecording)

    @Query("SELECT * FROM user_pr_record WHERE workout_name is :workoutName")
    suspend fun getPersonalRecordOfThisWorkout(workoutName: String): UserPersonalRecording

    @Query("SELECT * FROM user_pr_record")
    fun getAllUserPersonalRecord(): LiveData<List<UserPersonalRecording>>
}