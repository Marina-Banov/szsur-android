package hr.uniri.szsur.ui.survey_questions

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import hr.uniri.szsur.R
import hr.uniri.szsur.data.model.ActiveSurveyResult
import hr.uniri.szsur.databinding.ActiveAnswerPercentageBinding
import hr.uniri.szsur.databinding.FragmentSurveyActiveResultsBinding
import kotlin.math.roundToInt

class SurveyActiveResultsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyActiveResultsBinding
    private var firestore = FirebaseFirestore.getInstance()

    @SuppressLint("LongLogTag")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_survey_active_results,
            container,
            false
        )
        binding.lifecycleOwner = this

        val surveyModel = SurveyActiveResultsFragmentArgs.fromBundle(requireArguments()).surveyModel
        binding.survey = surveyModel

        val answersList = binding.answersList
        val answers = surveyModel.activeQuestionChoices

        binding.returnButton.setOnClickListener{
            requireActivity().onBackPressed()
        }

        firestore.collection("surveys").document(surveyModel.documentId)
            .collection("results")
            .get()
            .addOnSuccessListener {
                val answerCount = Array(answers.size) { 0 }
                var sum = 0
                for(document in it){
                    val result = document.toObject(ActiveSurveyResult::class.java)
                    val index = answers.indexOf(result.q)
                    answerCount[index] += 1
                    sum += 1
                }
                for (i in answers.indices){
                    val percentage = (answerCount[i].toDouble() / sum.toDouble()) * 100.0
                    val view = generateActiveAnswerPercentageBar(answers[i], percentage.roundToInt())
                    answersList.addView(view)
                }

            }
            .addOnFailureListener{
                Log.d("SurveyActiveResultsFragment", it.toString())
            }

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