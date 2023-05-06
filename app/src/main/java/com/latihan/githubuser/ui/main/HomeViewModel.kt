package com.latihan.githubuser.ui.main

import androidx.lifecycle.ViewModel
import com.latihan.githubuser.data.UserRepository

class HomeViewModel(private val repository: UserRepository): ViewModel() {
    fun findUserByUserName(username: String) = repository.findUserListByUsername(username)
}