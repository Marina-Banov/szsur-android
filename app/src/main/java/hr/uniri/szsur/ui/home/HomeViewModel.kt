package hr.uniri.szsur.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.data.network.ResultWrapper.*
import hr.uniri.szsur.data.repository.EventsRepository
import hr.uniri.szsur.util.SharedPreferenceUtils
import hr.uniri.szsur.util.SharedPreferenceUtils.Fields
import hr.uniri.szsur.util.filterByTags
import hr.uniri.szsur.util.search
import hr.uniri.szsur.util.sortByOrganisation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList


class HomeViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _events = MutableLiveData<ArrayList<Event>>()
    private val _displayEvents = MutableLiveData<ArrayList<Event>>()
    val displayEvents: LiveData<ArrayList<Event>>
        get() = _displayEvents

    init {
        getEvents()
    }

    private fun getEvents() {
        coroutineScope.launch {
            if (EventsRepository.events.value?.size == 0) {
                val response = EventsRepository.get()
                EventsRepository.events.value =
                    when (response) {
                        is NetworkError -> {
                            Log.i("getEvents", "NO CONNECTION")
                            ArrayList()
                        }
                        is GenericError -> {
                            Log.i("getEvents", "ERROR ${response.code}")
                            ArrayList()
                        }
                        is Success -> response.value as ArrayList<Event>
                    }
            }

            val events = sortByOrganisation(
                EventsRepository.events.value,
                SharedPreferenceUtils.getString(Fields.SELECTED_ORGANIZATION, "SZSUR")
            )
            _events.value = events
            _displayEvents.value = events
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
