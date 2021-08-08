package hr.uniri.szsur.ui.survey_questions

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.FragmentSurveyActiveQuestionBinding
import hr.uniri.szsur.databinding.FragmentSurveyQuestionsBinding
import hr.uniri.szsur.ui.survey_details.SurveyDetailsFragmentDirections


class SurveyActiveQuestionFragment : Fragment() {

    private lateinit var binding: FragmentSurveyActiveQuestionBinding
    private lateinit var answer: String
    private var firestore = FirebaseFirestore.getInstance()
    private var userRepository = UserRepository.getInstance(FirebaseFirestore.getInstance())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val application = requireActivity().application
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey_active_question, container, false)
        binding.lifecycleOwner = this

        val surveyModel = SurveyActiveQuestionFragmentArgs.fromBundle(requireArguments()).surveyModel
        binding.survey = surveyModel

        userRepository.user.observe(viewLifecycleOwner, {
            if (it.solved_surveys.contains(surveyModel.documentId)){
                requireActivity().onBackPressed()
            }
        })
        val radioGroup = binding.activeSurveyRadioGroup
        var checked = false
        for (item in surveyModel.activeQuestionChoices){
            var radioButton = RadioButton(radioGroup.context)
            radioButton.setText(item)
            radioGroup.addView(radioButton)
            if(!checked){
                radioButton.isChecked = true
                checked = true
                answer = radioButton.text.toString()
            }
        }

        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            answer = radio.text.toString()

        })

        binding.sendAnswersButton.setOnClickListener {
            val data = hashMapOf(
                "q" to answer,
            )
            firestore.collection("surveys").document(surveyModel.documentId)
                .collection("results").add(data)
                .addOnSuccessListener {
                    firestore.collection("users").document(Firebase.auth.currentUser!!.uid)
                       .update("solved_surveys", FieldValue.arrayUnion(surveyModel.documentId))
                    findNavController().navigate(
                        SurveyActiveQuestionFragmentDirections.
                    actionSurveyActiveQuestionFragmentToSurveyActiveResultsFragment(surveyModel))
                }
                .addOnFailureListener {
                    Log.d("SurveyQuestionsFragment", it.toString())
                    Toast.makeText(this.context, "Neuspješan unos odgovora, probajte ponovo", Toast.LENGTH_LONG).show()
                }
        }

        binding.returnButton.setOnClickListener {
            requireActivity().onBackPressed()
        }


        return binding.root


    }


}