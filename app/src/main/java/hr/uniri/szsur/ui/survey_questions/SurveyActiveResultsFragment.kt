package hr.uniri.szsur.ui.survey_questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.R
import hr.uniri.szsur.databinding.ActiveAnswerPercentageBinding
import hr.uniri.szsur.databinding.FragmentSurveyActiveResultsBinding
import hr.uniri.szsur.util.SurveyViewModelFactory


class SurveyActiveResultsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyActiveResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey_active_results, container, false)
        binding.lifecycleOwner = this

        val surveyModel = SurveyActiveQuestionFragmentArgs.fromBundle(requireArguments()).surveyModel
        val viewModelFactory = SurveyViewModelFactory(surveyModel, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SurveyActiveResultsViewModel::class.java)
        binding.viewModel = viewModel

        binding.returnButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        if (viewModel.results.value!!.isEmpty()) {
            viewModel.getSurveyResults()
        }

        viewModel.results.observe(viewLifecycleOwner, {
            for (e in it.entries) {
                val view = generateActiveAnswerPercentageBar(e.key, e.value)
                binding.answersList.addView(view)
            }
        })

        return binding.root
    }

    private fun generateActiveAnswerPercentageBar(answer: String, percentage: Int): View {
        val binding = ActiveAnswerPercentageBinding.inflate(LayoutInflater.from(context))
        binding.answer = answer
        binding.percentage = percentage
        binding.percentageText = "$percentage%"
        return binding.root
    }
}