package hr.uniri.szsur.ui.survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.R
import hr.uniri.szsur.databinding.FragmentSurveyBinding

class SurveyFragment : Fragment() {

    private lateinit var binding: FragmentSurveyBinding
    lateinit var viewModel: SurveyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey, container, false)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(SurveyViewModel::class.java)

        binding.searchFilter.selectedTags.observe(viewLifecycleOwner, {
            viewModel.updateSurveys(it)
        })

        binding.searchFilter.searchQuery.observe(viewLifecycleOwner, {
            viewModel.updateSurveys(it)
        })

        binding.pager.adapter = SurveyPagerAdapter(childFragmentManager)
        return binding.root
    }
}