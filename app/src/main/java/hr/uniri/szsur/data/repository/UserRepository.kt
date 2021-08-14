package hr.uniri.szsur.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.*
import hr.uniri.szsur.data.network.Api
import kotlinx.coroutines.*

object UserRepository {

    private const val TAG = "UserRepository"

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

    suspend fun updateFavorites(body: UpdateFavorite) = withContext(Dispatchers.IO) {
        try {
            val result = Api.retrofitService.updateFavorites(uid, body)
            true
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            false
        }
    }
}