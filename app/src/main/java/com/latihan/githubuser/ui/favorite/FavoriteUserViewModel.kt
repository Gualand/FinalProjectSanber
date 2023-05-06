package com.latihan.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.latihan.githubuser.data.UserRepository
import com.latihan.githubuser.data.local.entity.FavoriteEntity

class FavoriteUserViewModel(private val repository: UserRepository) : ViewModel() {
    fun getFavoritedUserList(): LiveData<List<FavoriteEntity>> = repository.getFavoritedUser()
}