package kr.valor.bal.data.local.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertUserInfo(item: UserInfo)

    @Query("SELECT * FROM user_info")
    fun getUserInfo(): LiveData<UserInfo?>
}