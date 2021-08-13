package hr.uniri.szsur.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.data.model.EventJson
import hr.uniri.szsur.data.model.Tags
import hr.uniri.szsur.data.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


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

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): User
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
}
