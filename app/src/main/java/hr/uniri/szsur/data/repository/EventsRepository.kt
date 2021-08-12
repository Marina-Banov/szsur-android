package hr.uniri.szsur.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.data.network.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object EventsRepository {

    private const val TAG = "EventsRepository"

    val events = MutableLiveData<ArrayList<Event>>()

    init {
        events.value = ArrayList()
    }

    suspend fun get() = withContext(Dispatchers.IO) {
        try {
            val events = Api.retrofitService.getEvents()
            val result = ArrayList<Event>()

            for (e in events) {
                val _e = Api.getEventFromJson(e)
                if (!e.online) {
                    val place = PlacesRepository.get(e.location, listOf(
                        Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,
                    ))
                    _e.setOnsiteLocation(place)
                }
                result.add(_e)
            }

            result
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            listOf()
        }
    }
}