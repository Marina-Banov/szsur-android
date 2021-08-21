package hr.uniri.szsur.ui.survey_details

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.chip.Chip
import hr.uniri.szsur.R
import hr.uniri.szsur.databinding.FragmentSurveyResultsDetailsBinding
import hr.uniri.szsur.util.GlideApp
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.util.SurveyViewModelFactory
import hr.uniri.szsur.util.handleClick


class SurveyResultsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentSurveyResultsDetailsBinding
    private lateinit var galery : LinearLayout
    private lateinit var dialog: Dialog

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val application = requireActivity().application
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey_results_details, container, false)
        binding.lifecycleOwner = this


        val surveyModel = SurveyResultsDetailsFragmentArgs.fromBundle(requireArguments()).surveyModel
        val viewModelFactory = SurveyViewModelFactory(surveyModel, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SurveyDetailsViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.updateFavorites(UserRepository.user.value!!.favorites)
        UserRepository.user.observe(viewLifecycleOwner, {
            viewModel.updateFavorites(it.favorites)
        })


        dialog = Dialog(requireContext())
        galery = binding.gallery

        viewModel.resultsImages.value?.forEach { ref ->
            Log.i("Survey", ref.toString())
            val imageView = ImageView(context)
            imageView.setPadding(20, 2, 20, 2)
            imageView.setScaleType(ScaleType.FIT_XY)
            GlideApp.with(this).load(ref).into(imageView)
            galery.addView(imageView)
            imageView.setOnClickListener {
                dialog.setContentView(R.layout.image_popup)
                val popupImage = dialog.findViewById<ImageView>(R.id.popup_image)
                dialog.show()
                GlideApp.with(popupImage).load(ref).into(popupImage)
            }

            imageView.requestLayout()
            imageView.layoutParams.height = 500
            imageView.layoutParams.width = 500


        }

        viewModel.survey.observe(viewLifecycleOwner, {
            for (tag in (it).tags) {
                val chip = layoutInflater.inflate(R.layout.layout_chip, binding.surveyTagGroup, false) as Chip
                chip.text = tag
                chip.isClickable = false
                binding.surveyTagGroup.addView(chip)
            }
        })


        binding.surveyResultsDetailsGoBackBtn.setOnClickListener { requireActivity().onBackPressed() }

        binding.favoritesButton.setOnClickListener {
            handleClick(viewModel.survey.value!!.documentId, false)
        }

        return binding.root
    }


}