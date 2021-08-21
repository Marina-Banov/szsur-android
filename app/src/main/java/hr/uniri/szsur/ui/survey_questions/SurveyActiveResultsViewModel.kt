package hr.uniri.szsur.ui.survey_questions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.ActiveSurveyResult
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.repository.SurveysRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class SurveyActiveResultsViewModel(s: Survey, app: Application) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var index = SurveysRepository.surveys.value!!.indexOf(s)

    private val _survey = MutableLiveData<Survey>()
    val survey: LiveData<Survey>
        get() = _survey

    private val _results = MutableLiveData<Map<String, Int>>()
    val results: LiveData<Map<String, Int>>
        get() = _results

    init {
        _survey.value = s
        _results.value = mapOf()

        val survey = SurveysRepository.surveys.value!![index]
        if (survey.results != null && survey.results!!.isNotEmpty()) {
            _results.value = survey.results!!
        }
    }

    fun getSurveyResults() {
        coroutineScope.launch {
            val res = SurveysRepository.getActiveSurveyResults(_survey.value!!.documentId)
            val percentages = calculatePercentages(res)
            SurveysRepository.surveys.value!![index].results = percentages
            _results.value = percentages
        }
    }

    private fun calculatePercentages(res: List<ActiveSurveyResult>): Map<String, Int> {
        val map = mutableMapOf<String, Int>()

        for (answer in res) {
            val value = map[answer.q]
            map[answer.q] = if (value == null) 1 else (value + 1)
        }

        for (key in _survey.value!!.activeQuestionChoices) {
            val value = map[key]
            map[key] = if (value == null) 0 else
                ((value.toDouble() / res.size.toDouble()) * 100.0).roundToInt()
        }

        return map
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
