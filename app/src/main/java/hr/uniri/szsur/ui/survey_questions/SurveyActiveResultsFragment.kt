package hr.uniri.szsur.ui.survey_questions

import android.annotation.SuppressLint
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import hr.uniri.szsur.R
import hr.uniri.szsur.data.model.ActiveSurveyResult
import hr.uniri.szsur.data.model.Question
import hr.uniri.szsur.data.model.Questions
import hr.uniri.szsur.data.model.SurveyModel
import hr.uniri.szsur.databinding.ActiveAnswerPercentageBinding
import hr.uniri.szsur.databinding.FragmentSurveyActiveResultsBinding
import hr.uniri.szsur.databinding.LayoutCardInputTextBinding
import hr.uniri.szsur.ui.survey_details.SurveyDetailsFragmentDirections
import kotlinx.android.synthetic.main.active_answer_percentage.*
import kotlin.math.roundToInt

class SurveyActiveResultsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyActiveResultsBinding
    private var firestore = FirebaseFirestore.getInstance()

    @SuppressLint("LongLogTag")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        val answers_list = binding.answersList
        val answers = surveyModel.activeQuestionChoices

        binding.returnButton.setOnClickListener{
            requireActivity().onBackPressed()
        }

        firestore.collection("surveys").document(surveyModel.documentId)
            .collection("results")
            .get()
            .addOnSuccessListener {
                var answerCount = Array(answers.size) { 0 }
                var sum = 0
                for(document in it){
                    var result = document.toObject(ActiveSurveyResult::class.java)
                    var index = answers.indexOf(result.q)
                    answerCount[index] += 1
                    sum += 1
                }
                for (i in 0..answers.size-1){
                    var percentage = (answerCount[i].toDouble() / sum.toDouble()) * 100.0
                    var view = generateActiveAnswerPercentageBar(answers[i], percentage.roundToInt())
                    answers_list.addView(view)
                }

            }
            .addOnFailureListener{
                Log.d("SurveyActiveResultsFragment", it.toString())
            }

        return binding.root
    }

    fun generateActiveAnswerPercentageBar(answer: String, percentage: Int): View {
        val binding = ActiveAnswerPercentageBinding.inflate(LayoutInflater.from(context))
        binding.answer = answer
        binding.percentage = percentage
        binding.percentageText = percentage.toString() + "%"
        return binding.root
    }
}