package hr.uniri.szsur.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.uniri.szsur.data.model.Filterable
import hr.uniri.szsur.data.repository.EventsRepository
import hr.uniri.szsur.data.repository.SurveysRepository
import hr.uniri.szsur.data.repository.SurveysRepository.SurveyFilter
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.util.filterByTags
import hr.uniri.szsur.util.search
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList

class FavoritesViewModel: ViewModel() {

    private val surveysRepository = SurveysRepository()
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _items = MutableLiveData<ArrayList<Filterable>>()
    val items: LiveData<ArrayList<Filterable>>
        get() = _items

    private val _favoriteItems = MutableLiveData<ArrayList<Filterable>>()
    val favoriteItems: LiveData<ArrayList<Filterable>>
        get() = _favoriteItems

    private val _displayItems = MutableLiveData<ArrayList<Filterable>>()
    val displayItems: LiveData<ArrayList<Filterable>>
        get() = _displayItems

    init {
        // TODO quite inefficient
        coroutineScope.launch {
            _items.value = EventsRepository.events.value as ArrayList<Filterable>
            _items.value!!.addAll(surveysRepository.get(SurveyFilter.ALL) as ArrayList<Filterable>)
            filterFavorites()
        }
    }

    fun filterFavorites() {
        if (_items.value == null) {
            return
        }
        _favoriteItems.value = ArrayList<Filterable>()
        for (f in UserRepository.user.value!!.favorites) {
            _items.value!!.find { it.documentId == f }?.let {
                _favoriteItems.value!!.add(it)
            }
        }
        _displayItems.value = _favoriteItems.value
    }

    fun updateFavorites(tags: ArrayList<String>?) {
        _favoriteItems.value?.let {
            _displayItems.value = filterByTags(it, tags)
        }
    }

    fun updateFavorites(query: String?) {
        _favoriteItems.value?.let {
            _displayItems.value = search(it, query)
        }
    }
}