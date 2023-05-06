package com.latihan.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.githubuser.ItemsItem
import com.latihan.githubuser.R
import com.latihan.githubuser.databinding.ActivityMainBinding
import com.latihan.githubuser.utils.ViewModelFactory
import com.latihan.githubuser.data.Result
import com.latihan.githubuser.data.local.preference.SettingPreferences
import com.latihan.githubuser.ui.adapter.UserAdapter
import com.latihan.githubuser.ui.detail.DetailUserActivity
import com.latihan.githubuser.ui.favorite.FavoriteUserActivity
import com.latihan.githubuser.ui.settings.SettingsActivity
import com.latihan.githubuser.ui.settings.SettingsViewModel
import com.latihan.githubuser.utils.SettingModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.recvUser.layoutManager = layoutManager

        val preferences = SettingPreferences.getInstance(dataStore)
        val settingsViewModel = ViewModelProvider(
            this,
            SettingModelFactory(preferences)
        )[SettingsViewModel::class.java]
        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        homeViewModel.findUserByUserName("naufal").observe(this@MainActivity) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Terjadi kesalahan" + result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    showLoading(false)
                    setUserList(result.data)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //mainViewModel.findUserListByUsername(query)
                homeViewModel.findUserByUserName(query).observe(this@MainActivity) {
                    when (it) {
                        is Result.Loading -> showLoading(true)
                        is Result.Error -> showLoading(false)
                        is Result.Success -> {
                            showLoading(false)
                            setUserList(it.data)
                        }
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this, FavoriteUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.settings -> {
                val intentSettings = Intent(this, SettingsActivity::class.java)
                startActivity(intentSettings)
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserList(listUsers: List<ItemsItem>) {
        val adapter = UserAdapter(listUsers)
        binding.recvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val detailIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                detailIntent.putExtra(DetailUserActivity.USERNAME, data.login)
                startActivity(detailIntent)
            }
        })
    }
}