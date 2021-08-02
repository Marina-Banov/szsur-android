package hr.uniri.szsur.util

import com.google.firebase.firestore.FirebaseFirestore
import hr.uniri.szsur.data.repository.UserRepository
import kotlinx.coroutines.*

fun handleClick(id: String, sendNotification: (() -> Unit)?) {
    val userRepository = UserRepository.getInstance(FirebaseFirestore.getInstance())

    if (userRepository.user.value!!.uid != "") {
        val favorites = userRepository.user.value!!.favorites
        val shouldSendNotification = !favorites.contains(id)
        favorites.apply {
            if (contains(id)) { remove(id) } else { add(id) }
        }

        val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        coroutineScope.launch {
            userRepository.updateFavorites(favorites)
            if (sendNotification != null && shouldSendNotification) {
                sendNotification()
            }
        }
    } else {
        // TODO
    }
}

fun isInFavourites(id: String): Boolean {
    val userRepository = UserRepository.getInstance(FirebaseFirestore.getInstance())

    if (userRepository.user.value!!.uid != "") {
        val favorites = userRepository.user.value!!.favorites
        favorites.apply {
            if (contains(id)) { return false } else { return true }
        }
        // TODO
    }
    return false
}