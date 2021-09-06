package hr.uniri.szsur.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.uniri.szsur.R
import hr.uniri.szsur.util.SharedPreferenceUtils
import hr.uniri.szsur.util.SharedPreferenceUtils.Fields

open class BaseThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val resId = R.style::class.java.getId("Theme_" + SharedPreferenceUtils.getString(
            Fields.SELECTED_ORGANIZATION, "SZSUR"))
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