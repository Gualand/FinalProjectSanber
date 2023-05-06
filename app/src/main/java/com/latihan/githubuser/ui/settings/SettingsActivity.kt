package com.latihan.githubuser.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.latihan.githubuser.databinding.ActivitySettingsBinding
import com.latihan.githubuser.data.local.preference.SettingPreferences
import com.latihan.githubuser.ui.main.dataStore
import com.latihan.githubuser.utils.SettingModelFactory


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = SettingPreferences.getInstance(dataStore)
        val settingsViewModel = ViewModelProvider(this, SettingModelFactory(preferences)).get(
            SettingsViewModel::class.java
        )

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.swTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.swTheme.isChecked = false
            }
        }

        binding.swTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSetting(isChecked)
        }

    }
}