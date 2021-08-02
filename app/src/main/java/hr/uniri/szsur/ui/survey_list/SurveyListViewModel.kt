package hr.uniri.szsur.ui.survey_list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.SurveyModel
import java.util.ArrayList

class SurveyListViewModel() : ViewModel()  {

    private val _surveys = MutableLiveData<ArrayList<SurveyModel>>()
    val surveys: LiveData<ArrayList<SurveyModel>>
        get() = _surveys

    fun setSurveys(s: ArrayList<SurveyModel>) {
        _surveys.value = s
    }
}