package hr.uniri.szsur.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.network.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object SurveysRepository {
    enum class SurveyFilter(val value: String) {
        ALL(""),
        PUBLISHED("true"),
        UNPUBLISHED("false"),
    }

    private const val TAG = "SurveysRepository"

    val surveys = MutableLiveData<ArrayList<Survey>>()

    init {
        surveys.value = ArrayList()
    }

    suspend fun get(filter: SurveyFilter) = withContext(Dispatchers.IO) {
        try {
            if (filter == SurveyFilter.ALL) {
                Api.retrofitService.getSurveys()
            } else {
                Api.retrofitService.getSurveys(filter.value)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            ArrayList()
        }
    }

    suspend fun getQuestions(id: String) = withContext(Dispatchers.IO) {
        try {
            Api.retrofitService.getSurveyQuestions(id)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    suspend fun getResults(id: String, active: Boolean) = withContext(Dispatchers.IO) {
        try {
            if (active) {
                Api.retrofitService.getActiveSurveyResults(id)
            } else {
                listOf()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            listOf()
        }
    }

    suspend fun addResults(id: String, body: Any) = withContext(Dispatchers.IO) {
        try {
            Api.retrofitService.addSurveyResults(id, body)
            true
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            false
        }
    }
}
