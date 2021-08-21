package hr.uniri.szsur.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.network.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object SurveysRepository {

    private const val TAG = "SurveysRepository"

    val surveys = MutableLiveData<ArrayList<Survey>>()

    init {
        surveys.value = ArrayList()
    }

    suspend fun get() = withContext(Dispatchers.IO) {
        try {
            Api.retrofitService.getSurveys()
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
            listOf()
        }
    }

    suspend fun getActiveSurveyResults(id: String) = withContext(Dispatchers.IO) {
        try {
            Api.retrofitService.getActiveSurveyResults(id)
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
