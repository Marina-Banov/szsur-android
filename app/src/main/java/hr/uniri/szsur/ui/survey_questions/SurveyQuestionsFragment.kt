package hr.uniri.szsur.ui.survey_questions

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.R
import hr.uniri.szsur.data.model.Question
import hr.uniri.szsur.data.model.QuestionType
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.databinding.*


class SurveyQuestionsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyQuestionsBinding
    private var answers = hashMapOf<String, Any>()
    private lateinit var survey: Survey


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        survey = SurveyQuestionsFragmentArgs.fromBundle(requireArguments()).surveyModel
        val viewModel = ViewModelProvider(this).get(SurveyQuestionsViewModel::class.java)

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
            if (validateAnswers()) {
                viewModel.addSurveyResults(survey.documentId, answers)
            } else {
                Toast.makeText(
                    context,
                    "Svako pitanje označeno sa * mora biti ispunjeno!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.isRequestSuccessful.observe(viewLifecycleOwner, {
            if (it == true) {
                val dialog = initSuccessDialog()
                dialog.show()
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

    private fun validateAnswers() : Boolean {
        if (survey.questions == null)
            return true
        for (q in survey.questions!!) {
            if (!q.required) continue
            if (answers[q.order] is String && answers[q.order] == "") return false
            if (answers[q.order] is MutableList<*> && (answers[q.order] as MutableList<*>).isEmpty()) return false
        }
        return true
    }

    private fun initSuccessDialog(): Dialog {
        val dialog = Dialog(this.requireContext())

        val binding: SurveyAnsweredDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.survey_answered_dialog,
            null,
            false
        )

        dialog.setContentView(binding.root)
        binding.surveyTitle.text = survey.title
        binding.goBackButton.setOnClickListener {
            val fmManager: FragmentManager = requireActivity().supportFragmentManager
            fmManager.popBackStack()
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        return dialog
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
        answers[q.order] = ""
        val radioGroup = binding.radioGroup

        for (item in q.choices) {
            val radioButton = RadioButton(radioGroup.context)
            radioButton.text = item
            radioGroup.addView(radioButton)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            answers[q.order] = radio.text
        }

        return binding.root
    }

}
