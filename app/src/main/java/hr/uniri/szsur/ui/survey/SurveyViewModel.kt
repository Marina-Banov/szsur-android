package hr.uniri.szsur.ui.survey

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.network.ResultWrapper.*
import hr.uniri.szsur.data.repository.SurveysRepository
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
                val response = SurveysRepository.get()
                SurveysRepository.surveys.value =
                    when (response) {
                        is NetworkError -> {
                            Log.i("getSurveys", "NO CONNECTION")
                            ArrayList()
                        }
                        is GenericError -> {
                            Log.i("getSurveys", "ERROR ${response.code}")
                            ArrayList()
                        }
                        is Success -> response.value as ArrayList<Survey>
                    }
            }

            val surveys = sortByOrganisation(
                SurveysRepository.surveys.value,
                SharedPreferenceUtils.getString(Fields.SELECTED_ORGANIZATION, "SZSUR")
            )
            _surveys.value = surveys
            _displaySurveys.value = surveys
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

    fun sortByOrganisation(organisation: String?) {
        _surveys.value?.let {
            _displaySurveys.value = sortByOrganisation(it, organisation)
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
