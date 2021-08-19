package hr.uniri.szsur.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.ui.survey_details.SurveyDetailsViewModel
import hr.uniri.szsur.ui.survey_questions.SurveyActiveQuestionViewModel
import hr.uniri.szsur.ui.survey_questions.SurveyActiveResultsViewModel


@Suppress("UNCHECKED_CAST")
class SurveyViewModelFactory(
    private val survey: Survey,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SurveyDetailsViewModel::class.java) -> {
                SurveyDetailsViewModel(survey, application) as T
            }
            modelClass.isAssignableFrom(SurveyActiveQuestionViewModel::class.java) -> {
                SurveyActiveQuestionViewModel(survey, application) as T
            }
            modelClass.isAssignableFrom(SurveyActiveResultsViewModel::class.java) -> {
                SurveyActiveResultsViewModel(survey, application) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
