package hr.uniri.szsur.ui.organisations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.uniri.szsur.data.model.UpdateOrganisation
import hr.uniri.szsur.data.network.ResultWrapper
import hr.uniri.szsur.data.repository.EnumsRepository
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.util.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class ChooseOrganisationViewModel : ViewModel() {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _organisations = MutableLiveData<ArrayList<String>>()
    val organisations: LiveData<ArrayList<String>>
        get() = _organisations

    init {
        if (UserRepository.token == "") {
            // TODO good reason to put splash screen
            Firebase.auth.currentUser!!.getIdToken(true).addOnCompleteListener { task ->
                if (!task.isSuccessful || task.result.token == null) {
                    // TODO what if task wasn't successful?
                    return@addOnCompleteListener
                }

                UserRepository.token = task.result.token!!
                getOrganisations()

            }
        }

    }

    private fun getOrganisations() {
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
            Log.i("getOrganisations", EnumsRepository.organisations.value.toString())
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


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}