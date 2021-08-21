package hr.uniri.szsur.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
            UserRepository.user.value = UserRepository.get()
        }
    }

    private fun getTags() {
        coroutineScope.launch {
            if (EnumsRepository.tags.value?.size == 0) {
                EnumsRepository.tags.value =
                    EnumsRepository.get(EnumsRepository.TAGS) as ArrayList<String>
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}