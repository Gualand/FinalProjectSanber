package com.latihan.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
class FavoriteEntity(
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    var username: String,

    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null
)