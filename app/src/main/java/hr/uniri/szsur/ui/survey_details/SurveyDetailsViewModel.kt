package hr.uniri.szsur.ui.survey_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.StorageReference
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.FirestoreRepository
import hr.uniri.szsur.data.model.Question
import hr.uniri.szsur.data.repository.SurveysRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SurveyDetailsViewModel(survey: Survey, app: Application) : AndroidViewModel(app) {
    private var firestoreRepository = FirestoreRepository()
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _surveyModel = MutableLiveData<Survey>()
    val survey: LiveData<Survey>
        get() = _surveyModel

    private val _resultImages = MutableLiveData<List<StorageReference>>()
    val resultsImages : LiveData<List<StorageReference>>
        get() = _resultImages

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    init {
        val index = SurveysRepository.surveys.value!!.indexOf(survey)
        if (survey.questions?.size == 0) {
            getQuestions(index, survey.documentId)
        }
        _surveyModel.value = SurveysRepository.surveys.value!![index]
        _resultImages.value = firestoreRepository.getImageReferences(survey.resultImages)
    }

    private fun getQuestions(index: Int, id: String) {
        coroutineScope.launch {
            val questions = SurveysRepository.getQuestions(id) as List<Question>
            SurveysRepository.surveys.value!![index].questions = questions.sortedBy { it.order }
        }
    }

    fun updateFavorites(favorites: List<String>) {
        _isFavorite.value = favorites.contains(_surveyModel.value!!.documentId)
    }

}
