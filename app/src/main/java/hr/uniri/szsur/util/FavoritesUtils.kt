package hr.uniri.szsur.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import hr.uniri.szsur.R
import hr.uniri.szsur.data.model.UpdateFavorite
import hr.uniri.szsur.data.network.ResultWrapper.*
import hr.uniri.szsur.data.repository.UserRepository
import kotlinx.coroutines.*


fun handleClick(favoriteId: String, isEvent: Boolean, context: Context?) {
    if (!UserRepository.isUserRegistered()) {
        Toast.makeText(context, R.string.anonymous_no_like, Toast.LENGTH_LONG).show()
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
                Toast.makeText(context, R.string.no_connection, Toast.LENGTH_LONG).show()
            }
            is GenericError -> {
                Log.i("updateFavorites", "ERROR ${response.code}")
                Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_LONG).show()
            }
            is Success -> UserRepository.updateFavorites(liked, favoriteId)
        }
    }
}
