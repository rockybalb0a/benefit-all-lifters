package kr.valor.bal.data.local.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUserInfo(item: UserInfo)

    @Query("SELECT * FROM user_info LIMIT 1")
    fun getUserInfo(): LiveData<UserInfo?>
}