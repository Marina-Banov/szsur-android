package hr.uniri.szsur.util

import android.util.Log
import hr.uniri.szsur.data.model.UpdateFavorite
import hr.uniri.szsur.data.network.ResultWrapper.*
import hr.uniri.szsur.data.repository.UserRepository
import kotlinx.coroutines.*


fun handleClick(favoriteId: String, isEvent: Boolean) {
    if (!UserRepository.isUserRegistered()) {
        // TODO anonymous user
        return
    }

    val liked =
        !(UserRepository.user.value!!.favorites as ArrayList<String>).contains(favoriteId)

    val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    coroutineScope.launch {
        val response = UserRepository.updateFavorites(
            UpdateFavorite(liked, isEvent, favoriteId)
        )
        when (response) {
            is NetworkError -> {
                Log.i("updateFavorites", "NO CONNECTION")
                // TODO toast
            }
            is GenericError -> {
                Log.i("updateFavorites", "ERROR ${response.code}")
                // TODO toast
            }
            is Success -> UserRepository.updateFavorites(liked, favoriteId)
        }
    }
}
