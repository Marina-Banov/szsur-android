package hr.uniri.szsur.ui.survey_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.FragmentSurveyDetailsBinding
import hr.uniri.szsur.util.SurveyViewModelFactory
import hr.uniri.szsur.util.handleClick


class SurveyDetailsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey_details, container, false)
        binding.lifecycleOwner = this

        val surveyModel = SurveyDetailsFragmentArgs.fromBundle(requireArguments()).surveyModel
        val viewModelFactory = SurveyViewModelFactory(surveyModel, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SurveyDetailsViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.updateFavorites(UserRepository.user.value!!.favorites)
        UserRepository.user.observe(viewLifecycleOwner, {
            viewModel.updateFavorites(it.favorites)
            if (it.solvedSurveys.contains(surveyModel.documentId)){
                if (surveyModel.active){
                    binding.filloutSurveyButton.text = "PRIKAŽI REZULTATE"
                }else{
                    binding.filloutSurveyButton.visibility = View.GONE
                }

            }
        })

        viewModel.survey.observe(viewLifecycleOwner, {
            for (tag in (it).tags) {
                val chip = layoutInflater.inflate(R.layout.layout_chip, binding.surveyTagGroup, false) as Chip
                chip.text = tag
                chip.isClickable = false
                binding.surveyTagGroup.addView(chip)
            }
        })

        binding.surveyDetailsGoBackBtn.setOnClickListener { requireActivity().onBackPressed() }

        binding.filloutSurveyButton.setOnClickListener {
            if (viewModel.survey.value!!.active) {
                if (binding.filloutSurveyButton.text.equals("Riješi")) {
                    findNavController().navigate(SurveyDetailsFragmentDirections.
                    actionSurveyDetailsFragmentToSurveyActiveQuestionFragment(viewModel.survey.value!!))
                } else {
                    findNavController().navigate(SurveyDetailsFragmentDirections.
                    actionSurveyDetailsFragmentToSurveyActiveResultsFragment(viewModel.survey.value!!))
                }
            } else {
                findNavController().navigate(SurveyDetailsFragmentDirections.
                actionSurveyDetailsFragmentToSurveyQuestionsFragment(viewModel.survey.value!!))
            }
        }

        binding.favoritesButton.setOnClickListener {
            handleClick(viewModel.survey.value!!.documentId, false, context)
        }

        return binding.root
    }

}




