package com.latihan.githubuser.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.latihan.githubuser.ItemsItem
import com.latihan.githubuser.data.local.entity.FavoriteEntity
import com.latihan.githubuser.data.local.room.FavoriteDao
import com.latihan.githubuser.data.remote.response.DetailUserResponse
import com.latihan.githubuser.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao
) {

    fun findUserListByUsername(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserListByUsername(username)
            emit(Result.Success(response.items))
        } catch (e: Exception) {
            Log.d("UserRepository", "findUserListByUsername: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun findUserDetailsByUsername(username: String): LiveData<Result<DetailUserResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getUserDetail(username)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getFollowers(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowers(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollowing(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowing(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun addToFavorite(user: DetailUserResponse) {
        val favoriteUser = FavoriteEntity(user.login, user.avatarUrl)
        favoriteDao.insertFavorite(favoriteUser)
    }

    fun isFavorited(username: String): Boolean = favoriteDao.isFavorited(username)

    suspend fun deleteFavorite(username: String) {
        favoriteDao.deleteFavorite(username)
    }

    fun getFavoritedUser(): LiveData<List<FavoriteEntity>> {
        return favoriteDao.getFavoriteUsers()
    }

    companion object {

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: FavoriteDao
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, favoriteDao)
        }.also { instance = it }
    }
}