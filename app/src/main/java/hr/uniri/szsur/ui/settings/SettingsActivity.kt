package hr.uniri.szsur.ui.settings

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import hr.uniri.szsur.R
import hr.uniri.szsur.ui.BaseThemeActivity
import hr.uniri.szsur.ui.MainActivity
import hr.uniri.szsur.ui.login.LoginActivity

class SettingsActivity : BaseThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}