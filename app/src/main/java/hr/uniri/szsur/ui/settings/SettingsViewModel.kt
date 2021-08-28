package hr.uniri.szsur.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.network.ResultWrapper
import hr.uniri.szsur.data.repository.EnumsRepository
import hr.uniri.szsur.data.repository.SurveysRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.E

class SettingsViewModel: ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _organisations = MutableLiveData<ArrayList<String>>()
    val organisations: LiveData<ArrayList<String>>
        get() = _organisations

    init {
       getOrganisations()
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
            _organisations.value = EnumsRepository.organisations.value
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}