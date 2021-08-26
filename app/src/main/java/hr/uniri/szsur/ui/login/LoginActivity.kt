package hr.uniri.szsur.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
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
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.util.AppActivityResult
import hr.uniri.szsur.util.SharedPreferenceUtils


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val googleSignInLauncher: AppActivityResult<Intent, ActivityResult> =
        AppActivityResult.registerActivityForResult(this)

    private val onAuthComplete = OnCompleteListener<AuthResult> { task ->
        _loading.value = false
        if (task.isSuccessful) {
            SharedPreferenceUtils.remove(USER_EMAIL_KEY)
            navigateToMainActivity()
        } else {
            showLoginFailed()
        }
    }

    companion object {
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
        auth = Firebase.auth

        val nightMode = SharedPreferenceUtils.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        if (nightMode != null) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInLauncher.setOnActivityResult { result -> signInWithGoogle(result) }
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser != null) {
            navigateToMainActivity()
        } else {
            val emailLink = intent.data.toString()
            val email = SharedPreferenceUtils.getString(USER_EMAIL_KEY, "")
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
                SharedPreferenceUtils.putString(USER_EMAIL_KEY, email)
                Toast.makeText(applicationContext, R.string.email_sent, Toast.LENGTH_LONG).show()
            } else {
                showLoginFailed()
            }
        }
    }

    fun firebaseAuthGoogle() {
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    private fun signInWithGoogle(result: ActivityResult) {
        if (result.resultCode != RESULT_OK) {
            showLoginFailed()
            return
        }

        val data = result.data
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

    fun firebaseAuthAnonymous() {
        _loading.value = true
        auth.signInAnonymously().addOnCompleteListener(this, onAuthComplete)
    }

    private fun navigateToMainActivity() {
        UserRepository.uid = Firebase.auth.currentUser!!.uid
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoginFailed() {
        _loading.value = false
        Toast.makeText(applicationContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
    }
}