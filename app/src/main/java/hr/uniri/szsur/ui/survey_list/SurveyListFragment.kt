package hr.uniri.szsur.ui.survey_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.FragmentSurveyListBinding
import hr.uniri.szsur.ui.MainFragmentDirections
import hr.uniri.szsur.ui.survey.SurveyFragment
import hr.uniri.szsur.util.filterByStatus

class SurveyListFragment : Fragment() {
    private lateinit var binding: FragmentSurveyListBinding
    private var userRepository = UserRepository.getInstance(FirebaseFirestore.getInstance())
    private var isPublished: Boolean = false

    companion object {
        const val IS_PUBLISHED = "isPublished"

        fun newInstance(isPublished: Boolean): SurveyListFragment {
            val bundle = Bundle(1)
            bundle.putBoolean(IS_PUBLISHED, isPublished)
            val fragment = SurveyListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isPublished = requireArguments().getBoolean(IS_PUBLISHED)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey_list, container, false)
        binding.lifecycleOwner = this

        val viewModel = ViewModelProvider(this).get(SurveyListViewModel::class.java)
        binding.viewModel = viewModel
        (parentFragment as SurveyFragment).viewModel.displaySurveys.observe(
            viewLifecycleOwner,
            { viewModel.setSurveys(filterByStatus(it, isPublished)) }
        )

        binding.surveyRecyclerView.adapter = SurveyListAdapter {
            if (it.published) {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToSurveyResultsDetailsFragment(it)
                )
            } else {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToSurveyDetailsFragment(it)
                )
            }
        }

        userRepository.user.observe(viewLifecycleOwner, {
            binding.surveyRecyclerView.adapter!!.notifyDataSetChanged()
        })

        return binding.root
    }

}