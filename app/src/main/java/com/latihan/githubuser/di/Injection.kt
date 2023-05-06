package com.latihan.githubuser.di

import android.content.Context
import com.latihan.githubuser.data.UserRepository
import com.latihan.githubuser.data.local.room.FavoriteDatabase
import com.latihan.githubuser.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favoriteDao()
        return UserRepository.getInstance(apiService, dao)
    }
}