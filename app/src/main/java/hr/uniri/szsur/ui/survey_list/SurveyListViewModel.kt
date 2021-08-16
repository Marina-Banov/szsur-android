package hr.uniri.szsur.ui.survey_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.Survey
import java.util.ArrayList

class SurveyListViewModel() : ViewModel()  {

    private val _surveys = MutableLiveData<ArrayList<Survey>>()
    val surveys: LiveData<ArrayList<Survey>>
        get() = _surveys

    fun setSurveys(s: ArrayList<Survey>) {
        _surveys.value = s
    }
}