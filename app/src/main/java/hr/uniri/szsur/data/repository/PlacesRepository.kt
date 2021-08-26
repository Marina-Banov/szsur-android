package hr.uniri.szsur.data.repository

import android.util.Log
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


object PlacesRepository {

    private const val TAG = "PlacesRepository"

    lateinit var client: PlacesClient

    suspend fun get(placeId: String, placeFields: List<Place.Field>) = withContext(Dispatchers.IO) {
        try {
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)
            client.fetchPlace(request).await().place
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            null
        }
    }
}
