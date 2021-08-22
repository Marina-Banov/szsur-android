package hr.uniri.szsur.ui.survey_questions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.model.SurveyAnswer
import hr.uniri.szsur.data.repository.SurveysRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SurveyQuestionsViewModel(s: Survey, app: Application) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var index: Int = 0
    var answers = hashMapOf<String, Any>()

    private val _survey = MutableLiveData<Survey>()
    val survey: LiveData<Survey>
        get() = _survey

    private val _isRequestSuccessful = MutableLiveData<Boolean?>(null)
    val isRequestSuccessful: LiveData<Boolean?>
        get() = _isRequestSuccessful

    init {
        _survey.value = s
        index = SurveysRepository.surveys.value!!.indexOf(s)
        getQuestions()
    }

    private fun getQuestions() {
        coroutineScope.launch {
            if (_survey.value!!.questions?.size == 0) {
                val questions = SurveysRepository.getQuestions(_survey.value!!.documentId)
                SurveysRepository.surveys.value!![index].questions = questions.sortedBy { it.order }
                _survey.value = SurveysRepository.surveys.value!![index]
            }
        }
    }

    fun addSurveyResults() {
        coroutineScope.launch {
           _isRequestSuccessful.value =
               SurveysRepository.addResults(
                   SurveyAnswer(_survey.value!!.documentId, false, answers)
               )
        }
    }

    fun validateAnswers() : Boolean {
        if (_survey.value!!.questions == null)
            return true
        for (q in _survey.value!!.questions!!) {
            if (!q.required) continue
            if (answers[q.order] is String && answers[q.order] == "") return false
            if (answers[q.order] is MutableList<*> && (answers[q.order] as MutableList<*>).isEmpty()) return false
        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}