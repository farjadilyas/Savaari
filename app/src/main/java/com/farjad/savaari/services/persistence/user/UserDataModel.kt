package com.farjad.savaari.services.persistence.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserDataModel(
        @PrimaryKey val userID: Int,
        val username: String?,
        @ColumnInfo(name = "first_name") val firstName: String?,
        @ColumnInfo(name = "last_name") val lastName: String?,
        @ColumnInfo(name = "email_address") val emailAddress: String?,
        @ColumnInfo(name = "last_update", defaultValue = "CURRENT_TIMESTAMP") val lastUpdate: Long
)