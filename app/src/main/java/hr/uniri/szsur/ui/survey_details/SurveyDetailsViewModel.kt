package hr.uniri.szsur.ui.survey_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.StorageReference
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.repository.FirebaseStorageRepository


class SurveyDetailsViewModel(s: Survey, app: Application) : AndroidViewModel(app) {

    private val _survey = MutableLiveData<Survey>()
    val survey: LiveData<Survey>
        get() = _survey

    private val _resultImages = MutableLiveData<List<StorageReference>>()
    val resultsImages : LiveData<List<StorageReference>>
        get() = _resultImages

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    init {
        _survey.value = s
        _resultImages.value = FirebaseStorageRepository.getImageReferences(s.resultImages)
    }

    fun updateFavorites(favorites: List<String>) {
        _isFavorite.value = favorites.contains(_survey.value!!.documentId)
    }
}
