package hr.uniri.szsur.ui.survey_questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.uniri.szsur.data.repository.SurveysRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SurveyQuestionsViewModel: ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _isRequestSuccessful = MutableLiveData<Boolean?>(null)
    val isRequestSuccessful: LiveData<Boolean?>
        get() = _isRequestSuccessful

    fun addSurveyResults(id: String, answers: Any) {
        coroutineScope.launch {
           _isRequestSuccessful.value = SurveysRepository.addResults(id, answers)
        }
    }
}