package hr.uniri.szsur.ui.survey_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.StorageReference
import hr.uniri.szsur.data.model.SurveyModel
import hr.uniri.szsur.data.FirestoreRepository

class SurveyDetailsViewModel(s: SurveyModel, app: Application) : AndroidViewModel(app) {
    private var firestoreRepository = FirestoreRepository()

    private val _surveyModel = MutableLiveData<SurveyModel>()
    val surveyModel: LiveData<SurveyModel>
        get() = _surveyModel

    private val _resultImages = MutableLiveData<List<StorageReference>>()
    val resultsImages : LiveData<List<StorageReference>>
        get() = _resultImages

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    init {
        _surveyModel.value = s
        _resultImages.value = firestoreRepository.getImageReferences(s.resultImages)
    }

    fun updateFavorites(favorites: ArrayList<String>) {
        _isFavorite.value = favorites.contains(_surveyModel.value!!.documentId)
    }

}
