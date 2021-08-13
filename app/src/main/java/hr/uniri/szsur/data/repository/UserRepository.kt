package hr.uniri.szsur.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.User
import hr.uniri.szsur.data.network.Api
import kotlinx.coroutines.*

object UserRepository {

    private const val TAG = "UserRepository"
    private const val FAVORITES = "favorites"

    var uid: String = ""
    var token: String = ""
    val user = MutableLiveData<User>()

    init {
        user.value = User()
    }

    suspend fun get() = withContext(Dispatchers.IO) {
        try {
            Api.retrofitService.getUser(uid)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            User()
        }
    }

    suspend fun updateFavorites(favorites: List<String>) = withContext(Dispatchers.IO) {
     /*   try {
            val result = userDocument.update(FAVORITES, favorites).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            false
        }*/
    }
}