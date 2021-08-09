package hr.uniri.szsur.util

import android.content.Context
import android.content.SharedPreferences
import hr.uniri.szsur.App

object SharedPreferenceUtils {
  private const val PREFS_NAME = "SZSUR_SHARED_PREFS"

  private val sharedPrefs: SharedPreferences?
    get() = App.appContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

  fun putString(keyName: String, value: String) {
    val editor: SharedPreferences.Editor = sharedPrefs?.edit() ?: return
    editor.putString(keyName, value)
    editor.apply()
  }

  fun putInt(keyName: String, value: Int) {
    val editor: SharedPreferences.Editor = sharedPrefs?.edit() ?: return
    editor.putInt(keyName, value)
    editor.apply()
  }

  fun putBoolean(keyName: String, value: Boolean) {
    val editor: SharedPreferences.Editor = sharedPrefs?.edit() ?: return
    editor.putBoolean(keyName, value)
    editor.apply()
  }

  fun getString(keyName: String, defaultValue: String): String? {
    return sharedPrefs?.getString(keyName, defaultValue)
  }

  fun getInt(keyName: String, defaultValue: Int): Int? {
    return sharedPrefs?.getInt(keyName, defaultValue)
  }

  fun getBoolean(keyName: String, defaultValue: Boolean): Boolean? {
    return sharedPrefs?.getBoolean(keyName, defaultValue)
  }

  fun clear() {
    val editor: SharedPreferences.Editor = sharedPrefs?.edit() ?: return
    editor.clear()
    editor.apply()
  }

  fun remove(keyName: String) {
    val editor: SharedPreferences.Editor = sharedPrefs?.edit() ?: return
    editor.remove(keyName)
    editor.apply()
  }
}
