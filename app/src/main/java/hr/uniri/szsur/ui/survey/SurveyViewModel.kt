package hr.uniri.szsur.ui.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.uniri.szsur.data.model.Survey
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

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _surveys = MutableLiveData<ArrayList<Survey>>()
    private val _displaySurveys = MutableLiveData<ArrayList<Survey>>()
    val displaySurveys: LiveData<ArrayList<Survey>>
        get() = _displaySurveys

    init {
        getSurveys()
    }

    private fun getSurveys() {
        coroutineScope.launch {
            if (SurveysRepository.surveys.value?.size == 0) {
                SurveysRepository.surveys.value = SurveysRepository.get(SurveyFilter.ALL) as ArrayList<Survey>
            }
            _surveys.value = SurveysRepository.surveys.value
            _displaySurveys.value = SurveysRepository.surveys.value
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