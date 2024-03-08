package com.example.weatherforecast.settingPage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityHomeBinding
import com.example.weatherforecast.databinding.ActivitySettingBinding
import com.example.weatherforecast.model.utils.Constants
import com.example.weatherforecast.model.utils.PreferenceManager

class SettingActivity : AppCompatActivity() {
    lateinit var binding :ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
//    fun getSelectedLanguageRadioButton(context: Context): RadioButton? {
//        val savedLanguage = PreferenceManager.getLanguage(context)
//        return when (savedLanguage) {
//            "English" -> findViewById<RadioButton>(R.id.englishRadioButton)
//            "Arabic" -> findViewById<RadioButton>(R.id.arabicRadioButton)
//            else -> null
//        }
//    }
//
//    fun getSelectedUnitRadioButton(context: Context): RadioButton? {
//        val savedUnit = PreferenceManager.getTempUnit(context)
//        return when (savedUnit) {
//            "Celsius" -> findViewById<RadioButton>(R.id.celsiusRadioButton)
//            "Fahrenheit" -> findViewById<RadioButton>(R.id.fahrenheitRadioButton)
//            "Kelvin" -> findViewById<RadioButton>(R.id.kelvinRadioButton)
//            else -> null
//        }
//    }
//    private fun setSelectedRadioButtons() {
//        val selectedLanguageRadioButton = getSelectedLanguageRadioButton(this)
//        selectedLanguageRadioButton?.isChecked = true
//
//        val selectedUnitRadioButton = getSelectedUnitRadioButton(this)
//        selectedUnitRadioButton?.isChecked = true
//    }
}