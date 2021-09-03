package hr.uniri.szsur.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.uniri.szsur.data.model.User
import hr.uniri.szsur.data.network.ResultWrapper.*
import hr.uniri.szsur.data.repository.EnumsRepository
import hr.uniri.szsur.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class MainViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        if (UserRepository.token == "") {
            // TODO good reason to put splash screen
            Firebase.auth.currentUser!!.getIdToken(true).addOnCompleteListener { task ->
                if (!task.isSuccessful || task.result.token == null) {
                    // TODO what if task wasn't successful?
                    return@addOnCompleteListener
                }

                UserRepository.token = task.result.token!!
                getUser()
                getTags()
            }
        }
    }

    private fun getUser() {
        coroutineScope.launch {
            val response = UserRepository.get()
            UserRepository.user.value =
                when (response) {
                    is NetworkError -> {
                        Log.i("getUser", "NO CONNECTION")
                        User()
                    }
                    is GenericError -> {
                        Log.i("getUser", "ERROR ${response.code}")
                        User()
                    }
                    is Success -> response.value
                }
        }
    }

    private fun getTags() {
        if (EnumsRepository.tags.value?.size != 0)
            return

        coroutineScope.launch {
            val response = EnumsRepository.get(EnumsRepository.TAGS)
            EnumsRepository.tags.value =
                when (response) {
                    is GenericError -> {
                        Log.i("getTags", "ERROR ${response.code}")
                        ArrayList()
                    }
                    is Success -> response.value.values as ArrayList<String>
                    else -> ArrayList()
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
