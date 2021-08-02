package hr.uniri.szsur.ui.event_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.Event

class EventDetailsViewModel(e: Event, app: Application) : AndroidViewModel(app) {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    init {
        _event.value = e
    }

    fun updateFavorites(favorites: ArrayList<String>) {
        _isFavorite.value = favorites.contains(_event.value!!.documentId)
    }
}