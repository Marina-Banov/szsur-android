package hr.uniri.szsur.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.uniri.szsur.data.model.Filterable
import hr.uniri.szsur.data.repository.EventsRepository
import hr.uniri.szsur.data.repository.SurveysRepository
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.util.SharedPreferenceUtils
import hr.uniri.szsur.util.filterByTags
import hr.uniri.szsur.util.search
import hr.uniri.szsur.util.sortByOrganisation
import java.util.ArrayList


class FavoritesViewModel: ViewModel() {

    private val _favoriteItems = MutableLiveData<ArrayList<Filterable>>()
    val favoriteItems: LiveData<ArrayList<Filterable>>
        get() = _favoriteItems

    private val _displayItems = MutableLiveData<ArrayList<Filterable>>()
    val displayItems: LiveData<ArrayList<Filterable>>
        get() = _displayItems

    init {
        filterFavorites()
    }

    fun filterFavorites() {
        _favoriteItems.value = ArrayList<Filterable>()
        for (f in UserRepository.user.value!!.favorites) {
            EventsRepository.events.value!!.find { it.documentId == f }?.let {
                _favoriteItems.value!!.add(it)
            }
            SurveysRepository.surveys.value!!.find { it.documentId == f }?.let {
                _favoriteItems.value!!.add(it)
            }
        }
        val favorites = sortByOrganisation(
            _favoriteItems.value,
            SharedPreferenceUtils.getString("selectedOrganisation", "SZSUR")
        )
        _favoriteItems.value = favorites
        _displayItems.value = favorites
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