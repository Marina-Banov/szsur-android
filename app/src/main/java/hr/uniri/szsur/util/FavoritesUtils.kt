package hr.uniri.szsur.util

import hr.uniri.szsur.data.repository.UserRepository
import kotlinx.coroutines.*

fun handleClick(id: String, sendNotification: (() -> Unit)?) {
    if (UserRepository.user.value!!.uid != "") {
        val favorites = UserRepository.user.value!!.favorites
        val shouldSendNotification = !favorites.contains(id)
        favorites.apply {
           // TODO uncomment
           // if (contains(id)) { remove(id) } else { add(id) }
        }

        val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        coroutineScope.launch {
            UserRepository.updateFavorites(favorites)
            if (sendNotification != null && shouldSendNotification) {
                sendNotification()
            }
        }
    } else {
        // TODO
    }
}

fun isInFavourites(id: String): Boolean {
    if (UserRepository.user.value!!.uid != "") {
        val favorites = UserRepository.user.value!!.favorites
        favorites.apply {
            if (contains(id)) { return false } else { return true }
        }
        // TODO
    }
    return false
}