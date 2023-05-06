package com.latihan.githubuser.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.latihan.githubuser.R
import com.latihan.githubuser.data.remote.response.DetailUserResponse
import com.latihan.githubuser.databinding.ActivityDetailUserBinding
import com.latihan.githubuser.utils.ViewModelFactory
import com.latihan.githubuser.data.Result
import com.latihan.githubuser.ui.adapter.SectionPagerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    private var userDetail: DetailUserResponse? = null
    private var isUserFavorited: Boolean = false

    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = if (Build.VERSION.SDK_INT > 33) {
            intent.getStringExtra(USERNAME)
        } else {
            @Suppress("DEPRECIATION")
            intent.getStringExtra(USERNAME)
        }


        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (username != null) {
            detailViewModel.apply {
                lifecycleScope.launch(Dispatchers.IO) {
                    isUserFavorited = isFavorited(username)
                    if (isUserFavorited) {
                        binding.fabFav.setImageDrawable(ContextCompat.getDrawable(this@DetailUserActivity, R.drawable.baseline_favorite_24))
                    } else {
                        binding.fabFav.setImageDrawable(ContextCompat.getDrawable(this@DetailUserActivity, R.drawable.baseline_favorite_border_24))
                    }
                }
                getDetail(username).observe(this@DetailUserActivity) { result ->
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            setUserData(result.data)
                            binding.fabFav.setOnClickListener {
                                if (isUserFavorited) {
                                    deleteFavorited(result.data.login)
                                    isUserFavorited = false
                                    binding.fabFav.setImageDrawable(ContextCompat.getDrawable(this@DetailUserActivity, R.drawable.baseline_favorite_border_24))
                                    Toast.makeText(
                                        this@DetailUserActivity,
                                        "Delete Favorited",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    addToFavorite(result.data)
                                    isUserFavorited = true
                                    binding.fabFav.setImageDrawable(ContextCompat.getDrawable(this@DetailUserActivity, R.drawable.baseline_favorite_24))
                                    Toast.makeText(
                                        this@DetailUserActivity,
                                        "Favorited",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(
                                this@DetailUserActivity,
                                "Terjadi kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }
        }


        val sectionsPagerAdapter = SectionPagerAdapter(this)
        sectionsPagerAdapter.username = username.toString()
        val viewPager: ViewPager2 = findViewById(R.id.vPager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserData(userData: DetailUserResponse) {
        userDetail = userData
        binding.txtvFullname.text = userData.name
        binding.txtvUsername.text = userData.login
        binding.txtvFollowers.text = resources.getString(R.string.followers, userData.followers)
        binding.txtvFollowing.text = resources.getString(R.string.following, userData.following)
        binding.txtvLocation.text = userData.location
        binding.txtvRepositories.text =
            resources.getString(R.string.repositories, userData.publicRepos)
        Glide.with(this).load(userData.avatarUrl).into(binding.imvAvatar)

        binding.txtvFullname.visibility = if (userData.name == null) View.GONE else View.VISIBLE
        binding.txtvLocation.visibility = if (userData.location == null) View.GONE else View.VISIBLE

    }

    private fun showLoading(isLoading: Boolean) {
        binding.pBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}