package hr.uniri.szsur.ui.survey_questions

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import hr.uniri.szsur.R
import hr.uniri.szsur.data.model.Question
import hr.uniri.szsur.data.model.QuestionType
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.databinding.*


class SurveyQuestionsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyQuestionsBinding
    private var answers = hashMapOf<String, Any>()
    private lateinit var survey: Survey
    private var firestore = FirebaseFirestore.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        survey = SurveyQuestionsFragmentArgs.fromBundle(requireArguments()).surveyModel

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey_questions, container, false)
        binding.lifecycleOwner = this

        binding.surveyTitle.text = survey.title

        binding.questionsList.apply {
            if (survey.questions == null)
                return@apply

            for (i in survey.questions!!) {
                when (i.type) {
                    QuestionType.SINGLE_CHOICE.value -> {
                        val view = generateRadioGroupCard(i)
                        addView(view)
                    }
                    QuestionType.MULTIPLE_CHOICE.value -> {
                        val view = generateCheckboxCard(i)
                        addView(view)
                    }
                    QuestionType.INPUT_TEXT.value -> {
                        val view = generateInputTextCard(i)
                        addView(view)
                    }
                }
            }
        }

        binding.sendAnswersButton.setOnClickListener {
            if (validateAnswers()) saveAnswersAndRedirect(survey)
            else Toast.makeText(this.context, "Svako pitanje označeno sa * mora biti ispunjeno!", Toast.LENGTH_LONG).show()
        }

        binding.returnButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    private fun validateAnswers() : Boolean {
        if (survey.questions == null)
            return true
        for (i in survey.questions!!.indices) {
            if (!survey.questions!![i].required) continue
            if (answers[i.toString()] is String && answers[i.toString()] == "") return false
            if (answers[i.toString()] is MutableList<*> && (answers[i.toString()] as MutableList<*>).isEmpty()) return false
        }
        return true
    }

    private fun saveAnswersAndRedirect(survey: Survey) {
        firestore.collection("surveys").document(survey.documentId)
            .collection("results").add(answers)
            .addOnSuccessListener {
                firestore.collection("users").document(Firebase.auth.currentUser!!.uid)
                        .update("solved_surveys", FieldValue.arrayUnion(survey.documentId))
                val dialog = Dialog(this.requireContext())
                dialog.setContentView(R.layout.survey_answered_dialog)
                val surveyDialogTitle = dialog.findViewById<TextView>(R.id.survey_title)
                surveyDialogTitle.text = survey.title
                val goBackButton = dialog.findViewById<Button>(R.id.go_back_button)
                goBackButton.setOnClickListener {
                    val fmManager: FragmentManager = requireActivity().supportFragmentManager
                    fmManager.popBackStack()
                    dialog.dismiss()

                }
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
            }
            .addOnFailureListener {
                Log.d("SurveyQuestionsFragment", it.toString())
                Toast.makeText(this.context, "Neuspješan unos odgovora, probajte ponovo", Toast.LENGTH_LONG).show()
            }
    }

    private fun generateInputTextCard(q: Question): View {
        val binding = LayoutCardInputTextBinding.inflate(LayoutInflater.from(context))
        binding.question = q
        answers[q.order] = ""
        val inputText = binding.inputText
        inputText.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                answers[q.order] = text.toString()
            }
        }

        return binding.root
    }

    private fun generateCheckboxCard(q: Question): View {
        val binding = LayoutCardCheckboxBinding.inflate(LayoutInflater.from(context))
        binding.question = q
        answers[q.order] = mutableListOf<String>()
        val checkboxContainer = binding.checkboxContainer
        for (item in q.choices){
            val checkBox = CheckBox(checkboxContainer.context)
            checkBox.text = item
            checkboxContainer.addView(checkBox)

            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    val list = answers[q.order] as MutableList<String>
                    list.add(buttonView.text.toString())
                    answers[q.order] = list
                }else{
                    val list = answers[q.order] as MutableList<String>
                    if (list.contains(buttonView.text.toString())) list.remove(buttonView.text.toString())
                    answers[q.order] = list
                }
            }
        }
        return binding.root
    }

    private fun generateRadioGroupCard(q: Question): View {
        val binding = LayoutCardRadiobuttonBinding.inflate(LayoutInflater.from(context))

        binding.question = q
        val radioGroup = binding.radioGroup
        var checked = false
        for (item in q.choices){

            val radioButton = RadioButton(radioGroup.context)
            radioButton.text = item
            radioGroup.addView(radioButton)
            if(!checked){
                radioButton.isChecked = true
                checked = true
                answers[q.order] = radioButton.text
            }
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            answers[q.order] = radio.text

        }

        return binding.root
    }

}
