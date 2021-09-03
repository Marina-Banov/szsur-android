package hr.uniri.szsur.ui.organisations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.ui.MainActivity

class ChooseOrganisationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_organisation)
        Log.i("ChooseOrganisation", "hereAC")
    }

     fun navigateToMainActivity() {
        UserRepository.uid = Firebase.auth.currentUser!!.uid
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}