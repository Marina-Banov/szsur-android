package hr.uniri.szsur.ui.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.uniri.szsur.data.model.SurveyModel
import hr.uniri.szsur.data.repository.SurveysRepository.SurveyFilter
import hr.uniri.szsur.data.repository.SurveysRepository
import hr.uniri.szsur.util.filterByTags
import hr.uniri.szsur.util.search
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList

class SurveyViewModel: ViewModel() {
    private val surveysRepository = SurveysRepository()
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _surveys = MutableLiveData<ArrayList<SurveyModel>>()
    private val _displaySurveys = MutableLiveData<ArrayList<SurveyModel>>()
    val displaySurveys: LiveData<ArrayList<SurveyModel>>
        get() = _displaySurveys

    init {
        getSurveys()
    }

    private fun getSurveys() {
        coroutineScope.launch {
            _surveys.value = surveysRepository.get(SurveyFilter.ALL)
            _displaySurveys.value = _surveys.value
        }
    }

    fun updateSurveys(tags: ArrayList<String>) {
        _surveys.value?.let {
            _displaySurveys.value = filterByTags(it, tags)
        }
    }

    fun updateSurveys(query: String) {
        _surveys.value?.let {
            _displaySurveys.value = search(it, query)
        }
    }
}