package hr.uniri.szsur.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.network.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object EnumsRepository {

    private const val TAG = "EnumsRepository"
    const val TAGS = "tags"

    val tags = MutableLiveData<ArrayList<String>>()

    init {
        tags.value = ArrayList()
    }

    suspend fun get(enumType: String) = withContext(Dispatchers.IO) {
        try {
            val result = when (enumType) {
                TAGS -> Api.retrofitService.getTags()
                else -> null
            }
            result?.values ?: listOf()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            listOf()
        }
    }

}