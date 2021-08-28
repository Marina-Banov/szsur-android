package hr.uniri.szsur.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hr.uniri.szsur.data.model.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


private const val BASE_URL = "https://us-central1-szsur-7e723.cloudfunctions.net/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val loggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

private val okhttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor())
    .addInterceptor(loggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okhttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface ApiService {
    @GET("enums/tags")
    suspend fun getTags(): Tags

    @GET("enums/organisations")
    suspend fun getOrganisations(): Organisations

    @GET("events")
    suspend fun getEvents(): List<EventJson>

    @GET("surveys")
    suspend fun getSurveys(): List<Survey>

    @GET("surveys/{id}/questions")
    suspend fun getSurveyQuestions(@Path("id") id: String): List<Question>

    @GET("surveys/{id}/results")
    suspend fun getActiveSurveyResults(@Path("id") id: String): List<ActiveSurveyResult>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserJson

    @PUT("users/{id}/favorites")
    suspend fun updateFavorites(@Path("id") id: String, @Body body: UpdateFavorite): ResponseBody

    @PUT("users/{id}/surveys")
    suspend fun addSurveyResults(@Path("id") id: String, @Body body: Any): ResponseBody
}

object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
