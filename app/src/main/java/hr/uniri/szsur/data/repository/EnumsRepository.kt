package hr.uniri.szsur.data.repository

import androidx.lifecycle.MutableLiveData
import hr.uniri.szsur.data.model.Organisations
import hr.uniri.szsur.data.model.Tags
import hr.uniri.szsur.data.network.Api
import hr.uniri.szsur.data.network.ResultWrapper
import hr.uniri.szsur.data.network.NetworkUtils


object EnumsRepository {

    const val TAGS = "tags"
    const val ORGANISATIONS = "organisations"

    val tags = MutableLiveData<ArrayList<String>>()
    val organisations = MutableLiveData<ArrayList<String>>()

    init {
        tags.value = ArrayList()
        organisations.value = ArrayList()
    }

    suspend fun get(enumType: String): ResultWrapper<Tags>? {
        return when (enumType) {
            TAGS -> NetworkUtils.safeApiCall { Api.retrofitService.getTags() }
            else -> null
        }
    }

    suspend fun getOrganisations(): ResultWrapper<Organisations>?{
        return NetworkUtils.safeApiCall { Api.retrofitService.getOrganisations() }
    }

}
