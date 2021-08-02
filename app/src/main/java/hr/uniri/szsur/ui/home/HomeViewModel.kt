package hr.uniri.szsur.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.data.repository.EventsRepository
import hr.uniri.szsur.util.filterByTags
import hr.uniri.szsur.util.search
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList

class HomeViewModel : ViewModel() {

    private val eventsRepository = EventsRepository.getInstance(FirebaseFirestore.getInstance())
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _events = MutableLiveData<ArrayList<Event>>()
    private val _displayEvents = MutableLiveData<ArrayList<Event>>()
    val displayEvents: LiveData<ArrayList<Event>>
        get() = _displayEvents

    init {
        coroutineScope.launch {
            val list = eventsRepository.get()
            _events.value = list
            _displayEvents.value = list
        }
    }

    fun updateEvents(tags: ArrayList<String>) {
        _events.value?.let {
            _displayEvents.value = filterByTags(it, tags)
        }
    }

    fun updateEvents(query: String) {
        _events.value?.let {
            _displayEvents.value = search(it, query)
        }
    }
}