package com.latihan.githubuser.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.latihan.githubuser.data.UserRepository
import com.latihan.githubuser.di.Injection
import com.latihan.githubuser.ui.*
import com.latihan.githubuser.ui.detail.DetailViewModel
import com.latihan.githubuser.ui.favorite.FavoriteUserViewModel
import com.latihan.githubuser.ui.follow.FollowViewModel
import com.latihan.githubuser.ui.main.HomeViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> return HomeViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> return DetailViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(FollowViewModel::class.java) -> return FollowViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(FavoriteUserViewModel::class.java) -> return FavoriteUserViewModel(
                userRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}