package hr.uniri.szsur.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.util.SingletonHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventsRepository private constructor(db: FirebaseFirestore) {

    private val TAG = "EventsRepository"
    private val COLLECTION_NAME = "events"

    companion object : SingletonHolder<EventsRepository, FirebaseFirestore>(::EventsRepository)

    private val eventsCollection = db.collection(COLLECTION_NAME)
    val events = MutableLiveData<ArrayList<Event>>()

    init {
        events.value = ArrayList()
    }

    suspend fun get() = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = eventsCollection.get().await()
            val result = ArrayList<Event>()
            for (event in querySnapshot) {
                val e = event.toObject(Event::class.java)
                if (!e.online) {
                    e.googlePlace = PlacesRepository.get(e.location, listOf(
                        Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,
                    ))
                }
                result.add(e)
            }
            result
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            ArrayList()
        }
    }
}