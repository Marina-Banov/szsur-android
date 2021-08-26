package hr.uniri.szsur.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.data.network.Api
import hr.uniri.szsur.data.network.NetworkUtils
import hr.uniri.szsur.data.network.ResultWrapper
import hr.uniri.szsur.data.network.ResultWrapper.*


object EventsRepository {

    val events = MutableLiveData<ArrayList<Event>>()

    init {
        events.value = ArrayList()
    }

    suspend fun get(): ResultWrapper<List<Event>> {
        return when(val result = NetworkUtils.safeApiCall { Api.retrofitService.getEvents() }) {
            is Success -> {
                val events = ArrayList<Event>()
                for (_e in result.value) {
                    val e = _e.getEventFromJson()
                    if (!_e.online) {
                        val place = PlacesRepository.get(_e.location, listOf(
                            Place.Field.ID,
                            Place.Field.NAME,
                            Place.Field.ADDRESS,
                            Place.Field.LAT_LNG,
                        ))
                        e.setOnsiteLocation(place)
                    }
                    events.add(e)
                }
                Success(events)
            }
            else -> result as ResultWrapper<Nothing>
        }
    }
}
