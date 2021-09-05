package hr.uniri.szsur.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.R
import hr.uniri.szsur.ui.settings.SettingsViewModel
import hr.uniri.szsur.util.SharedPreferenceUtils

open class BaseThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val resId = R.style::class.java.getId("Theme_" + SharedPreferenceUtils.getString("selectedOrganisation", "SZUR"))
        setTheme(resId)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_theme)
    }

    inline fun <reified T: Class<*>> T.getId(resourceName: String): Int {
        return try {
            val idField = getDeclaredField (resourceName)
            idField.getInt(idField)
        } catch (e:Exception) {
            e.printStackTrace()
            -1
        }
    }


}