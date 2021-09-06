package hr.uniri.szsur.util

import android.content.Context
import android.content.SharedPreferences
import hr.uniri.szsur.App


object SharedPreferenceUtils {
  private const val PREFS_NAME = "SZSUR_SHARED_PREFS"

  enum class Fields {
    RECEIVE_NOTIFICATIONS_EVENTS,
    RECEIVE_NOTIFICATIONS_SURVEYS,
    NIGHT_MODE,
    FCM_TOKEN,
    USER_EMAIL_KEY,
    ORGANIZATIONS,
    SELECTED_ORGANIZATION,
  }

  private val sharedPrefs: SharedPreferences?
    get() = App.appContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

  fun putString(keyName: Fields, value: String) {
    val editor: SharedPreferences.Editor = sharedPrefs?.edit() ?: return
    editor.putString(keyName.name, value)
    editor.apply()
  }

  fun putInt(keyName: Fields, value: Int) {
    val editor: SharedPreferences.Editor = sharedPrefs?.edit() ?: return
    editor.putInt(keyName.name, value)
    editor.apply()
  }

  fun putBoolean(keyName: Fields, value: Boolean) {
    val editor: SharedPreferences.Editor = sharedPrefs?.edit() ?: return
    editor.putBoolean(keyName.name, value)
    editor.apply()
  }

  fun getString(keyName: Fields, defaultValue: String): String? {
    return sharedPrefs?.getString(keyName.name, defaultValue)
  }

  fun getInt(keyName: Fields, defaultValue: Int): Int? {
    return sharedPrefs?.getInt(keyName.name, defaultValue)
  }

  fun getBoolean(keyName: Fields, defaultValue: Boolean): Boolean? {
    return sharedPrefs?.getBoolean(keyName.name, defaultValue)
  }

  fun clear() {
    val editor: SharedPreferences.Editor = sharedPrefs?.edit() ?: return
    editor.clear()
    editor.apply()
  }

  fun remove(keyName: Fields) {
    val editor: SharedPreferences.Editor = sharedPrefs?.edit() ?: return
    editor.remove(keyName.name)
    editor.apply()
  }
}
