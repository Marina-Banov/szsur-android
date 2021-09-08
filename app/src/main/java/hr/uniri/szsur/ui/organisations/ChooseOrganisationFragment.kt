package hr.uniri.szsur.ui.organisations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.R
import hr.uniri.szsur.databinding.FragmentChooseOrganisationBinding
import hr.uniri.szsur.util.SharedPreferenceUtils
import hr.uniri.szsur.util.SharedPreferenceUtils.Fields


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

        viewModel.organisations.observe(viewLifecycleOwner, {
            arrayAdapter = ArrayAdapter(
                this.requireContext(),
                R.layout.org_spinner_selected_item,
                it)
            arrayAdapter.setDropDownViewResource(R.layout.org_spinner_dropdown)
            binding.organisationSpinner.adapter = arrayAdapter
            binding.loadingPanel.visibility = View.GONE
            binding.organisationLinearLayout.visibility = View.VISIBLE
            binding.continueButton.visibility = View.VISIBLE
        })


        binding.continueButton.setOnClickListener {
            val selectedItem = binding.organisationSpinner.selectedItem.toString()
            viewModel.updateUsersOrganisation(selectedItem)
            SharedPreferenceUtils.putString(Fields.SELECTED_ORGANIZATION, selectedItem)
            (activity as ChooseOrganisationActivity).navigateToMainActivity()
        }
        return binding.root
    }
}
