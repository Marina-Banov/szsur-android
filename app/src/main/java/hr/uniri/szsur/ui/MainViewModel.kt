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
            UserRepository.user.value = when (val response = UserRepository.get()) {
                is NetworkError -> {
                    Log.i("getUser", "NO CONNECTION")
                    User()
                }
                is GenericError -> {
                    Log.i("getUser", "ERROR")
                    User()
                }
                is Success -> response.value.getUserFromJson()
            }
        }
    }

    private fun getTags() {
        if (EnumsRepository.tags.value?.size != 0)
            return

        coroutineScope.launch {
            EnumsRepository.tags.value =
                when (val response = EnumsRepository.get(EnumsRepository.TAGS)) {
                    is GenericError -> {
                        Log.i("getTags", "ERROR")
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
