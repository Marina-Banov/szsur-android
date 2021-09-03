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
    .add(UserAdapter())
    .add(EventAdapter())
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
    @EventJsonAdapter
    suspend fun getEvents(): List<Event>

    @GET("surveys")
    suspend fun getSurveys(): List<Survey>

    @GET("surveys/{id}/questions")
    suspend fun getSurveyQuestions(@Path("id") id: String): List<Question>

    @GET("surveys/{id}/results")
    suspend fun getActiveSurveyResults(@Path("id") id: String): List<ActiveSurveyResult>

    @GET("users/{id}")
    @UserJsonAdapter
    suspend fun getUser(@Path("id") id: String): User

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body body: Any): ResponseBody

    @PUT("users/{id}/favorites")
    suspend fun updateFavorites(@Path("id") id: String, @Body body: UpdateFavorite): ResponseBody

    @PUT("users/{id}/surveys")
    suspend fun addSurveyResults(@Path("id") id: String, @Body body: Any): ResponseBody

    @PUT("users/{id}")
    suspend fun updateOrganisation(@Path("id") id: String, @Body body: Any): ResponseBody
}

object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
