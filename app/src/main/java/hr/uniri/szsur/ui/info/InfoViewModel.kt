package hr.uniri.szsur.ui.info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.uniri.szsur.data.model.Organization
import hr.uniri.szsur.data.network.ResultWrapper.*
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.util.SharedPreferenceUtils
import hr.uniri.szsur.util.SharedPreferenceUtils.Fields
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class InfoViewModel: ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _organization = MutableLiveData<Organization>()
    val organization: LiveData<Organization>
        get() = _organization

    init {
        getOrganization()
    }

    private fun getOrganization() {
        coroutineScope.launch {
            val id = SharedPreferenceUtils.getString(Fields.SELECTED_ORGANIZATION, "SZSUR")
            val response = UserRepository.getOrganization(id ?: "SZSUR")
            _organization.value = when (response) {
                is NetworkError -> {
                    Log.i("getOrganization", "NO CONNECTION")
                    Organization()
                }
                is GenericError -> {
                    Log.i("getOrganization", "ERROR ${response.code}")
                    Organization()
                }
                is Success -> response.value
            }
        }
    }

}
