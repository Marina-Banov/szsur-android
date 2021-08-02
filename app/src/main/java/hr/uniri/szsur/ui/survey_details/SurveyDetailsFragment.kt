package hr.uniri.szsur.ui.survey_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import hr.uniri.szsur.R
import hr.uniri.szsur.data.model.Question
import hr.uniri.szsur.data.model.Questions
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.FragmentSurveyDetailsBinding
import hr.uniri.szsur.util.handleClick


class SurveyDetailsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyDetailsBinding
    private var userRepository = UserRepository.getInstance(FirebaseFirestore.getInstance())
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val application = requireActivity().application
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey_details, container, false)
        binding.lifecycleOwner = this

        val surveyModel = SurveyDetailsFragmentArgs.fromBundle(requireArguments()).surveyModel
        val viewModelFactory = SurveyDetailsViewModelFactory(surveyModel, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SurveyDetailsViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.updateFavorites(userRepository.user.value!!.favorites)
        userRepository.user.observe(viewLifecycleOwner, {
            viewModel.updateFavorites(it.favorites)
            if (it.solved_surveys.contains(surveyModel.documentId)){
                binding.filloutSurveyButton.visibility = View.GONE
            }
        })

        viewModel.surveyModel.observe(viewLifecycleOwner, {
            for (tag in (it!!).tags) {
                val chip = layoutInflater.inflate(R.layout.layout_chip, binding.surveyTagGroup, false) as Chip
                chip.text = tag
                chip.isClickable = false
                binding.surveyTagGroup.addView(chip)
            }
        })

        binding.surveyDetailsGoBackBtn.setOnClickListener { requireActivity().onBackPressed() }

        binding.filloutSurveyButton.setOnClickListener {
            firestore.collection("surveys").document(viewModel.surveyModel.value!!.documentId)
                    .collection("questions")
                    .orderBy("order", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener {
                        var questions : Questions = Questions()
                        for(document in it){
                            var q = document.toObject(Question::class.java)
                            questions.add(q)

                        }
                        findNavController().navigate(SurveyDetailsFragmentDirections.
                        actionSurveyDetailsFragmentToSurveyQuestionsFragment(questions,viewModel.surveyModel.value!! ))
                    }
                    .addOnFailureListener{
                        Log.d("SurveyDetailsFragment", it.toString())
                    }

        }

        binding.favoritesButton.setOnClickListener {
            handleClick(viewModel.surveyModel.value!!.documentId, null)
        }

        return binding.root
    }

}




