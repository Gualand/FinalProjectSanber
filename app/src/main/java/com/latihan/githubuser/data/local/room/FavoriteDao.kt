package com.latihan.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.latihan.githubuser.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getFavoriteUsers(): LiveData<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE username = :username")
    suspend fun deleteFavorite(username: String)

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE username = :username)")
    fun isFavorited(username: String): Boolean

}