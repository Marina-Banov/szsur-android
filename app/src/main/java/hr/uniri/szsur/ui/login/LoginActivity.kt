package hr.uniri.szsur.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.uniri.szsur.BuildConfig.APPLICATION_ID
import hr.uniri.szsur.BuildConfig.VERSION_NAME
import hr.uniri.szsur.ui.MainActivity
import hr.uniri.szsur.R


class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val onAuthComplete = OnCompleteListener<AuthResult> { task ->
        _loading.value = false
        if (task.isSuccessful) {
            sharedPreferences.edit().remove(USER_EMAIL_KEY).apply()
            navigateToMainActivity()
        } else {
            showLoginFailed()
        }
    }

    companion object {
        private const val GOOGLE_SIGN_IN_REQ_CODE = 123
        private const val USER_EMAIL_KEY = "USER_EMAIL"
        // private const val EMAIL_AUTH_LINK_KEY = "EMAIL_AUTH_LINK"
        private const val NIGHT_MODE = "NIGHT_MODE"
    }

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        auth = Firebase.auth

        val nightMode = sharedPreferences.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightMode)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            navigateToMainActivity()
        } else {
            val emailLink = intent.data.toString()
            val email = sharedPreferences.getString(USER_EMAIL_KEY, "")
            if (email != null && auth.isSignInWithEmailLink(emailLink)) {
                // TODO is it possible to reauthenticate user with these credentials?
                //  FirebaseAuthActionCodeException...
                //  could replace firebaseAuthSendEmail with firebaseAuthEmail
                // sharedPreferences.edit().putString(EMAIL_AUTH_LINK_KEY, emailLink).apply()
                _loading.value = true
                auth.signInWithEmailLink(email, emailLink).addOnCompleteListener(this, onAuthComplete)
            }
        }
    }

    override fun onBackPressed() {
        if (_loading.value == true) {
            _loading.value = false
        } else {
            super.onBackPressed()
        }
    }

    /* fun firebaseAuthEmail(email: String) {
        _loading.value = true
        val emailLink = sharedPreferences.getString(EMAIL_AUTH_LINK_KEY, "")
        if (emailLink != "null" && auth.isSignInWithEmailLink(emailLink!!)) {
            val credential = EmailAuthProvider.getCredentialWithLink(email, emailLink)
            auth.signInWithCredential(credential).addOnCompleteListener(this, onAuthComplete)
        } else {
           firebaseAuthSendEmail(email)
        }
    } */

    fun firebaseAuthSendEmail(email: String) {
        if (email == "") {
            return
        }

        _loading.value = true

        val actionCodeSettings = ActionCodeSettings.newBuilder()
                .setHandleCodeInApp(true)
                .setUrl("https://szsur.page.link")
                .setAndroidPackageName(APPLICATION_ID, true, VERSION_NAME)
                .build()

        auth.sendSignInLinkToEmail(email, actionCodeSettings).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sharedPreferences.edit().putString(USER_EMAIL_KEY, email).apply()
                Toast.makeText(applicationContext, R.string.email_sent, Toast.LENGTH_LONG).show()
            } else {
                showLoginFailed()
            }
        }
    }

    fun firebaseAuthGoogle() {
        startActivityForResult(googleSignInClient.signInIntent, GOOGLE_SIGN_IN_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQ_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                _loading.value = true
                auth.signInWithCredential(credential).addOnCompleteListener(this, onAuthComplete)
            } catch (e: ApiException) {
                showLoginFailed()
            }
        }
    }

    fun firebaseAuthAnonymous() {
        _loading.value = true
        auth.signInAnonymously().addOnCompleteListener(this, onAuthComplete)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoginFailed() {
        _loading.value = false
        Toast.makeText(applicationContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
    }
}