package hr.uniri.szsur.data.repository

import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.*
import hr.uniri.szsur.data.network.Api
import hr.uniri.szsur.data.network.ResultWrapper
import hr.uniri.szsur.data.network.NetworkUtils
import okhttp3.ResponseBody


object UserRepository {

    var uid: String = ""
    var token: String = ""
    val user = MutableLiveData<User>()

    init {
        user.value = User()
    }

    fun isUserRegistered(): Boolean {
        return user.value!!.uid != ""
    }

    suspend fun get(): ResultWrapper<User> {
        return NetworkUtils.safeApiCall { Api.retrofitService.getUser(uid) }
    }

    suspend fun getOrganization(id: String): ResultWrapper<Organization> {
        return NetworkUtils.safeApiCall { Api.retrofitService.getOrganization(id) }
    }

    suspend fun updateFavorites(body: UpdateFavorite): ResultWrapper<ResponseBody> {
        return NetworkUtils.safeApiCall { Api.retrofitService.updateFavorites(uid, body) }
    }

    suspend fun updateUser(body: Any): ResultWrapper<ResponseBody> {
        return NetworkUtils.safeApiCall { Api.retrofitService.updateUser(uid, body) }
    }

    fun updateFavorites(liked: Boolean, favoriteId: String) {
        (user.value!!.favorites as ArrayList<String>).apply {
            if (liked) { add(favoriteId) } else { remove(favoriteId) }
        }
        // Live data is not updated simply by updating the ArrayList
        // Must also update the reference
        user.value = user.value
    }
}
