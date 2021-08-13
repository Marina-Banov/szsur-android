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
        // TODO good reason to put splash screen

        if (UserRepository.token == "") {
            Firebase.auth.currentUser!!.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.token != null) {
                    UserRepository.token = task.result.token!!
                }
                // TODO what if task wasn't successful?

                coroutineScope.launch {
                    UserRepository.user.value = UserRepository.get()

                    if (EnumsRepository.tags.value?.size == 0) {
                        EnumsRepository.tags.value =
                            EnumsRepository.get(EnumsRepository.TAGS) as ArrayList<String>
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}