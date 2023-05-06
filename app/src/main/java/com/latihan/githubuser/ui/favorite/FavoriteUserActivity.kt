package com.latihan.githubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.githubuser.ItemsItem
import com.latihan.githubuser.databinding.ActivityFavoriteUserBinding
import com.latihan.githubuser.ui.adapter.UserAdapter
import com.latihan.githubuser.ui.detail.DetailUserActivity
import com.latihan.githubuser.utils.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding

    private val favoriteUserViewModel by viewModels<FavoriteUserViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.recvUserFavorite.layoutManager = layoutManager

        supportActionBar?.title = "Favorite Users"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteUserViewModel.getFavoritedUserList().observe(this) {user ->
            val items = ArrayList<ItemsItem>()
            user.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl!!)
                items.add(item)
            }
            setUserList(items)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserList(listUsers: List<ItemsItem>) {
        val adapter = UserAdapter(listUsers)
        binding.recvUserFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val detailIntent = Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
                detailIntent.putExtra(DetailUserActivity.USERNAME, data.login)
                startActivity(detailIntent)
            }
        })
    }
}