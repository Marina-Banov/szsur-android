package hr.uniri.szsur.data.repository

import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.ActiveSurveyResult
import hr.uniri.szsur.data.model.Question
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.model.SurveyAnswer
import hr.uniri.szsur.data.network.Api
import hr.uniri.szsur.data.network.ResultWrapper
import hr.uniri.szsur.data.network.NetworkUtils
import okhttp3.ResponseBody


object SurveysRepository {

    val surveys = MutableLiveData<ArrayList<Survey>>()

    init {
        surveys.value = ArrayList()
    }

    suspend fun get(): ResultWrapper<List<Survey>> {
        return NetworkUtils.safeApiCall { Api.retrofitService.getSurveys() }
    }

    suspend fun getQuestions(id: String): ResultWrapper<List<Question>> {
        return NetworkUtils.safeApiCall { Api.retrofitService.getSurveyQuestions(id) }
    }

    suspend fun getActiveSurveyResults(id: String): ResultWrapper<List<ActiveSurveyResult>> {
        return NetworkUtils.safeApiCall { Api.retrofitService.getActiveSurveyResults(id) }
    }

    suspend fun addResults(body: SurveyAnswer): ResultWrapper<ResponseBody> {
        return NetworkUtils.safeApiCall { Api.retrofitService.addSurveyResults(UserRepository.uid, body) }
    }
}
