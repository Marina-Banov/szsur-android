package hr.uniri.szsur.ui.survey_questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.FragmentSurveyActiveQuestionBinding
import hr.uniri.szsur.util.SurveyViewModelFactory


class SurveyActiveQuestionFragment : Fragment() {

    private lateinit var binding: FragmentSurveyActiveQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey_active_question, container, false)
        binding.lifecycleOwner = this

        val surveyModel = SurveyActiveQuestionFragmentArgs.fromBundle(requireArguments()).surveyModel
        val viewModelFactory = SurveyViewModelFactory(surveyModel, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SurveyActiveQuestionViewModel::class.java)
        binding.viewModel = viewModel

        UserRepository.user.observe(viewLifecycleOwner, {
            if (it.solvedSurveys.contains(surveyModel.documentId)) {
                requireActivity().onBackPressed()
            }
        })

        val radioGroup = binding.activeSurveyRadioGroup
        for (item in surveyModel.activeQuestionChoices) {
            val radioButton = RadioButton(radioGroup.context)
            radioButton.text = item
            radioGroup.addView(radioButton)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            viewModel.answers = hashMapOf("q" to radio.text.toString())
        }

        viewModel.isRequestSuccessful.observe(viewLifecycleOwner, {
            if (it == true) {
                val solvedSurveys = UserRepository.user.value!!.solvedSurveys as ArrayList<String>
                solvedSurveys.add(viewModel.survey.value!!.documentId)
                findNavController().navigate(
                    SurveyActiveQuestionFragmentDirections.
                    actionSurveyActiveQuestionFragmentToSurveyActiveResultsFragment(surveyModel)
                )
            } else if (it == false) {
                Toast.makeText(
                    context,
                    "Neuspješan unos odgovora, probajte ponovo",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        binding.returnButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

}