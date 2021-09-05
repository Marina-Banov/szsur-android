package hr.uniri.szsur.ui.organisations

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import hr.uniri.szsur.R
import hr.uniri.szsur.databinding.FragmentChooseOrganisationBinding
import hr.uniri.szsur.util.SharedPreferenceUtils


class ChooseOrganisationFragment : Fragment() {

    private lateinit var viewModel: ChooseOrganisationViewModel
    private lateinit var binding: FragmentChooseOrganisationBinding
    var organisations: ArrayList<String> = ArrayList()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_organisation, container, false)
        viewModel = ViewModelProvider(this).get(ChooseOrganisationViewModel::class.java)

        var storedOrganisations = SharedPreferenceUtils.getString("organisations", "")
        if (storedOrganisations != "" && storedOrganisations != null){
            organisations = storedOrganisations.split(",") as ArrayList<String>
        }

        viewModel.organisations.observe(viewLifecycleOwner, {
            arrayAdapter = ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it)

            binding.organisationSpinner.adapter = arrayAdapter
        })

        var storedSelectedOrganisation = SharedPreferenceUtils.getString("selectedOrganisation", "")
        if (storedSelectedOrganisation != "" && storedSelectedOrganisation != null){
            var position = organisations.indexOf(storedSelectedOrganisation)
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
                Toast.makeText(context, binding.organisationSpinner.selectedItem.toString(), Toast.LENGTH_SHORT).show()
                SharedPreferenceUtils.putString("selectedOrganisation", selectedItem)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.continueButton.setOnClickListener {
            (activity as ChooseOrganisationActivity).navigateToMainActivity()
        }
        return binding.root
    }
}
