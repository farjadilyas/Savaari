package com.farjad.savaari.services.persistence.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    fun save(userDataModel: UserDataModel)

    @Query("SELECT * FROM user_data WHERE userID = :userId")
    fun load(userId: Int): LiveData<UserDataModel>

    @Query("SELECT COUNT(*) FROM user_data WHERE userId == :userId AND CURRENT_TIME < last_update + :timeout")
    fun hasUser(userId : Int, timeout : Long) : Int

    @Delete
    fun delete(user: UserDataModel)
}