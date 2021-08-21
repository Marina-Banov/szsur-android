package hr.uniri.szsur.ui.survey_questions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.repository.SurveysRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SurveyActiveQuestionViewModel(s: Survey, app: Application) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _survey = MutableLiveData<Survey>()
    val survey: LiveData<Survey>
        get() = _survey

    private val _isRequestSuccessful = MutableLiveData<Boolean?>(null)
    val isRequestSuccessful: LiveData<Boolean?>
        get() = _isRequestSuccessful

    init {
        _survey.value = s
    }

    fun addSurveyResults(answers: HashMap<String, String>) {
        coroutineScope.launch {
            _isRequestSuccessful.value = SurveysRepository.addResults(_survey.value!!.documentId, answers)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
