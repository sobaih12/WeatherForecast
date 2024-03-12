package com.example.weatherforecast.settingPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherforecast.databinding.ActivitySettingBinding
import com.example.weatherforecast.model.utils.Constants
import com.example.weatherforecast.model.utils.PreferenceManager

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val language = PreferenceManager.getLanguage(this)
        val tempUnit = PreferenceManager.getTempUnit(this)

        binding.apply {
            when (language) {
                Constants.LANGUAGE_AR -> arabicRadioButton.isChecked = true
                Constants.LANGUAGE_EN -> englishRadioButton.isChecked = true
            }
            when (tempUnit) {
                Constants.UNITS_CELSIUS -> celsiusRadioButton.isChecked = true
                Constants.UNITS_FAHRENHEIT -> fahrenheitRadioButton.isChecked = true
                Constants.UNITS_DEFAULT -> kelvinRadioButton.isChecked = true
            }

            binding.apply {
                arabicRadioButton.setOnClickListener {
                    PreferenceManager.saveLanguage(this@SettingActivity, Constants.LANGUAGE_AR)
                }
                englishRadioButton.setOnClickListener {
                    PreferenceManager.saveLanguage(this@SettingActivity, Constants.LANGUAGE_EN)
                }
                celsiusRadioButton.setOnClickListener {
                    PreferenceManager.saveTempUnit(this@SettingActivity, Constants.UNITS_CELSIUS)
                }
                fahrenheitRadioButton.setOnClickListener {
                    PreferenceManager.saveTempUnit(this@SettingActivity, Constants.UNITS_FAHRENHEIT)
                }
                kelvinRadioButton.setOnClickListener {
                    PreferenceManager.saveTempUnit(this@SettingActivity, Constants.UNITS_DEFAULT)
                }
            }

        }
        binding.save.setOnClickListener {
            finish()
        }
    }
}