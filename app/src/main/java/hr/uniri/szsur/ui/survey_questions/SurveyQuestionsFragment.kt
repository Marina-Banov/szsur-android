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
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.R
import hr.uniri.szsur.data.model.Question
import hr.uniri.szsur.data.model.QuestionType
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.*
import hr.uniri.szsur.util.SurveyViewModelFactory


class SurveyQuestionsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyQuestionsBinding
    private lateinit var viewModel: SurveyQuestionsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val application = requireActivity().application
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey_questions, container, false)
        binding.lifecycleOwner = this

        val survey = SurveyQuestionsFragmentArgs.fromBundle(requireArguments()).surveyModel
        val viewModelFactory = SurveyViewModelFactory(survey, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SurveyQuestionsViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.survey.observe(viewLifecycleOwner, {
            if (it.questions != null) {
                generateQuestionCards()
            }
        })

        binding.sendAnswersButton.setOnClickListener {
            if (viewModel.validateAnswers()) {
                viewModel.addSurveyResults()
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
                val solvedSurveys = UserRepository.user.value!!.solvedSurveys as ArrayList<String>
                solvedSurveys.add(viewModel.survey.value!!.documentId)
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

    private fun initSuccessDialog(): Dialog {
        val dialog = Dialog(requireContext())

        val binding: SurveyAnsweredDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.survey_answered_dialog,
            null,
            false
        )

        dialog.setContentView(binding.root)
        binding.surveyTitle.text = viewModel.survey.value!!.title
        binding.goBackButton.setOnClickListener {
            dialog.dismiss()
            requireActivity().onBackPressed()
        }

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    private fun generateQuestionCards() {
        for (i in viewModel.survey.value!!.questions!!) {
            val view = when (i.type) {
                QuestionType.SINGLE_CHOICE.value -> {
                    generateRadioGroupCard(i)
                }
                QuestionType.MULTIPLE_CHOICE.value -> {
                    generateCheckboxCard(i)
                }
                QuestionType.INPUT_TEXT.value -> {
                    generateInputTextCard(i)
                }
                else -> continue
            }
            binding.questionsList.addView(view)
        }
    }

    private fun generateInputTextCard(q: Question): View {
        val binding = LayoutCardInputTextBinding.inflate(LayoutInflater.from(context))
        binding.question = q
        viewModel.answers[q.order] = ""
        val inputText = binding.inputText
        inputText.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                viewModel.answers[q.order] = text.toString()
            }
        }

        return binding.root
    }

    private fun generateCheckboxCard(q: Question): View {
        val binding = LayoutCardCheckboxBinding.inflate(LayoutInflater.from(context))
        binding.question = q
        viewModel.answers[q.order] = mutableListOf<String>()
        val checkboxContainer = binding.checkboxContainer
        for (item in q.choices) {
            val checkBox = CheckBox(checkboxContainer.context)
            checkBox.text = item
            checkboxContainer.addView(checkBox)

            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    val list = viewModel.answers[q.order] as MutableList<String>
                    list.add(buttonView.text.toString())
                    viewModel.answers[q.order] = list
                } else {
                    val list = viewModel.answers[q.order] as MutableList<String>
                    if (list.contains(buttonView.text.toString())) list.remove(buttonView.text.toString())
                    viewModel.answers[q.order] = list
                }
            }
        }
        return binding.root
    }

    private fun generateRadioGroupCard(q: Question): View {
        val binding = LayoutCardRadiobuttonBinding.inflate(LayoutInflater.from(context))

        binding.question = q
        viewModel.answers[q.order] = ""
        val radioGroup = binding.radioGroup

        for (item in q.choices) {
            val radioButton = RadioButton(radioGroup.context)
            radioButton.text = item
            radioGroup.addView(radioButton)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            viewModel.answers[q.order] = radio.text
        }

        return binding.root
    }

}
