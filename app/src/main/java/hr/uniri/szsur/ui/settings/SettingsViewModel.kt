package hr.uniri.szsur.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.uniri.szsur.data.model.UpdateOrganisation
import hr.uniri.szsur.data.model.User
import hr.uniri.szsur.data.network.ResultWrapper
import hr.uniri.szsur.data.repository.EnumsRepository
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.util.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _organisations = MutableLiveData<ArrayList<String>>()
    val organisations: LiveData<ArrayList<String>>
        get() = _organisations

    init {
       getOrganisations()
       getUserOrganisation()
    }

    fun getOrganisations() {
        if (EnumsRepository.organisations.value?.size != 0)
            return

        coroutineScope.launch {
            val response = EnumsRepository.getOrganisations()
            EnumsRepository.organisations.value =
                when (response) {
                    is ResultWrapper.GenericError -> {
                        Log.i("getOrganisations", "ERROR ${response.code}")
                        ArrayList()
                    }
                    is ResultWrapper.Success -> response.value.values as ArrayList<String>
                    else -> ArrayList()
                }
            _organisations.value = EnumsRepository.organisations.value
            var organisationString = _organisations.value.toString()
            organisationString = organisationString.drop(1).dropLast(1).replace(" ", "")
            SharedPreferenceUtils.putString("organisations", organisationString)
        }
    }

    fun updateUsersOrganisation(organisation: String){
        coroutineScope.launch {
            val body = UpdateOrganisation(organisation)
            val response = UserRepository.updateOrganisation(body)

            if (response is ResultWrapper.GenericError){
                Log.i("updateUsersOrganisation", "ERROR ${response.code}")
            }else{
                Log.i("updateUsersOrganisation", "Organisation updated succesfully")
            }
        }
    }

    private fun getUserOrganisation() {
        coroutineScope.launch {
            val response = UserRepository.get()
            UserRepository.user.value =
                when (response) {
                    is ResultWrapper.NetworkError -> {
                        Log.i("getUser", "NO CONNECTION")
                        User()
                    }
                    is ResultWrapper.GenericError -> {
                        Log.i("getUser", "ERROR ${response.code}")
                        User()
                    }
                    is ResultWrapper.Success -> response.value
                }
            SharedPreferenceUtils.putString("selectedOrganisation", UserRepository.user.value!!.organisation)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}