package hr.uniri.szsur.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.R
import hr.uniri.szsur.databinding.FragmentSettingsBinding
import hr.uniri.szsur.util.SharedPreferenceUtils
import hr.uniri.szsur.util.SharedPreferenceUtils.Fields


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    lateinit var viewModel: SettingsViewModel
    var organisations: ArrayList<String> = ArrayList()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        binding.logout.setOnClickListener {
            (activity as SettingsActivity).signOut()
        }

        val storedOrganisations = SharedPreferenceUtils.getString(Fields.ORGANIZATIONS, "")
        if (storedOrganisations != "" && storedOrganisations != null){
            organisations = storedOrganisations.split(",") as ArrayList<String>
        }

        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        arrayAdapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            organisations)

        viewModel.organisations.observe(viewLifecycleOwner, {
            organisations.clear()
            organisations.addAll(it)
            arrayAdapter.notifyDataSetChanged()
        })

        binding.organisationSpinner.adapter = arrayAdapter
        val storedSelectedOrganisation = SharedPreferenceUtils.getString(Fields.SELECTED_ORGANIZATION, "")
        if (storedSelectedOrganisation != "" && storedSelectedOrganisation != null){
            val position = organisations.indexOf(storedSelectedOrganisation)
            binding.organisationSpinner.setSelection(position)
        }

        binding.organisationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val selectedItem = binding.organisationSpinner.selectedItem.toString()
                viewModel.updateUsersOrganisation(binding.organisationSpinner.selectedItem.toString())
                Log.i("spinner", selectedItem)
                SharedPreferenceUtils.putString(Fields.SELECTED_ORGANIZATION, selectedItem)
                Toast.makeText(context, "Resetiraj aplikaciju za promjenu teme", Toast.LENGTH_LONG).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.swDarkMode.isChecked = getDarkMode() == Configuration.UI_MODE_NIGHT_YES
        binding.swEventNotification.isChecked = SharedPreferenceUtils.getBoolean(
            Fields.RECEIVE_NOTIFICATIONS_EVENTS, true) == true
        binding.swSurveyNotification.isChecked = SharedPreferenceUtils.getBoolean(
            Fields.RECEIVE_NOTIFICATIONS_SURVEYS, true) == true

        binding.swDarkMode.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isShown) { //ako nema ovog nakon aktiviranja switcha kod se neprestano pokrece
                when (getDarkMode()) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        SharedPreferenceUtils.putInt(Fields.NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_NO)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        SharedPreferenceUtils.putInt(Fields.NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_YES)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }
        }

        binding.swEventNotification.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isShown) { //ako nema ovog nakon aktiviranja switcha kod se neprestano pokrece
                val receiveNotification = SharedPreferenceUtils.getBoolean(Fields.RECEIVE_NOTIFICATIONS_EVENTS, true) == true
                SharedPreferenceUtils.putBoolean(Fields.RECEIVE_NOTIFICATIONS_EVENTS, !receiveNotification)
            }
        }

        binding.swSurveyNotification.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isShown) { //ako nema ovog nakon aktiviranja switcha kod se neprestano pokrece
                val receiveNotification = SharedPreferenceUtils.getBoolean(Fields.RECEIVE_NOTIFICATIONS_SURVEYS, true) == true
                SharedPreferenceUtils.putBoolean(Fields.RECEIVE_NOTIFICATIONS_SURVEYS, !receiveNotification)
            }
        }

        return binding.root
    }

    private fun getDarkMode(): Int {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    }

}
