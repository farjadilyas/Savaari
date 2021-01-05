package com.farjad.savaari.services.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.farjad.savaari.services.persistence.user.UserDao
import com.farjad.savaari.services.persistence.user.UserDataModel

/*
* Room automatically provides an implementation for this abstract class
*/

@Database(entities = [UserDataModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}