package hr.uniri.szsur.ui.survey_details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.data.model.Survey


@Suppress("UNCHECKED_CAST")
class SurveyDetailsViewModelFactory (private val survey: Survey, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SurveyDetailsViewModel::class.java)){
            return SurveyDetailsViewModel(survey, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
