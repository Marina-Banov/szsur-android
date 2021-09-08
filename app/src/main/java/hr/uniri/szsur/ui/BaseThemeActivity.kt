package hr.uniri.szsur.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.uniri.szsur.R
import hr.uniri.szsur.util.SharedPreferenceUtils
import hr.uniri.szsur.util.SharedPreferenceUtils.Fields

open class BaseThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        var theme = SharedPreferenceUtils.getString(Fields.SELECTED_ORGANIZATION, "SZUR")
        if(theme.equals("SZFZSRI") || theme.equals("SZMEDRI")){
            theme = "SZFZSRI_SZMEDRI"
        }else if (theme.equals("SZEFRI") || theme.equals("SZFMTU")){
            theme = "SZEFRI_SZFMTU"
        }
        val resId = R.style::class.java.getId("Theme_" + theme)
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