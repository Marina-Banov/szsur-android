package hr.uniri.szsur.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

        binding.swDarkMode.isChecked = getDarkMode() == Configuration.UI_MODE_NIGHT_YES
        binding.swEventNotification.isChecked = SharedPreferenceUtils.getBoolean(
            Fields.RECEIVE_NOTIFICATIONS_EVENTS, true) == true
        binding.swSurveyNotification.isChecked = SharedPreferenceUtils.getBoolean(
            Fields.RECEIVE_NOTIFICATIONS_SURVEYS, true) == true

        binding.applyButton.setOnClickListener {
            val selectedItem = binding.organisationSpinner.selectedItem.toString()
            SharedPreferenceUtils.putString(Fields.SELECTED_ORGANIZATION, selectedItem)
            viewModel.updateUsersOrganisation(selectedItem)
            requireActivity().recreate()
            (activity as SettingsActivity).returnToMain()
        }

        binding.swDarkMode.setOnCheckedChangeListener { buttonView, isChecked ->
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
