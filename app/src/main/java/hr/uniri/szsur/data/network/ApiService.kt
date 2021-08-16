package hr.uniri.szsur.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hr.uniri.szsur.data.model.*
import hr.uniri.szsur.data.repository.UserRepository
import okhttp3.OkHttpClient
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

    @GET("events")
    suspend fun getEvents(): List<EventJson>

    @GET("surveys")
    suspend fun getSurveys(): List<Survey>

    @GET("surveys")
    suspend fun getSurveys(@Query("published") published: String): List<Survey>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserJson

    @PUT("users/{id}/favorites")
    suspend fun updateFavorites(@Path("id") id: String, @Body body: UpdateFavorite): String
}

object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }

    fun getEventFromJson(eventJson: EventJson): Event {
        return Event(
            eventJson.id,
            eventJson.description,
            eventJson.image,
            eventJson.location,
            null,
            eventJson.online,
            eventJson.organisation,
            eventJson.startTime.toDate(),
            eventJson.tags,
            eventJson.title,
        )
    }

    fun getUserFromJson(userJson: UserJson): User {
        val favorites = ArrayList<String>()
        for (f in userJson.favorites) {
            favorites.add(f.id)
        }

        return User(
            UserRepository.uid,
            userJson.isAdmin,
            userJson.email,
            favorites,
            userJson.solvedSurveys,
        )
    }
}
