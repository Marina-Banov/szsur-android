package hr.uniri.szsur.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import hr.uniri.szsur.data.repository.EnumsRepository
import hr.uniri.szsur.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private var userRepository = UserRepository.getInstance(firestore)

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        // TODO good reason to put splash screen
        if (EnumsRepository.tags.value?.size == 0) {
            coroutineScope.launch {
                EnumsRepository.tags.value =
                    EnumsRepository.get(EnumsRepository.TAGS) as ArrayList<String>
            }
        }

        coroutineScope.launch {
            userRepository.user.value = userRepository.get()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}