package hr.uniri.szsur.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.data.model.HurrDurrEvent
import hr.uniri.szsur.data.model.Tags
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "https://us-central1-szsur-7e723.cloudfunctions.net/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("enums/tags")
    suspend fun getTags(): Tags

    @GET("events")
    suspend fun getEvents(): List<HurrDurrEvent>
}

object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
