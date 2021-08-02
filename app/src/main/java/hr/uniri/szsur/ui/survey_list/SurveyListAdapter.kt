package hr.uniri.szsur.ui.survey_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import hr.uniri.szsur.data.model.SurveyModel
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.LayoutCardSurveyBinding
import hr.uniri.szsur.util.DiffCallback
import hr.uniri.szsur.util.handleClick

class SurveyListAdapter(private val showDetailsListener: (survey: SurveyModel) -> Unit) :
        ListAdapter<SurveyModel, SurveyListAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(private var binding: LayoutCardSurveyBinding):
            RecyclerView.ViewHolder(binding.root) {
        private var userRepository = UserRepository.getInstance(FirebaseFirestore.getInstance())

        fun bind(survey: SurveyModel) {
            binding.survey = survey
            binding.isFavorite = userRepository.user.value!!.favorites.contains(survey.documentId)
            binding.favoritesButton.setOnClickListener {
                handleClick(survey.documentId, null)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCardSurveyBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val survey = getItem(position)
        viewHolder.itemView.setOnClickListener { showDetailsListener(survey) }
        viewHolder.bind(survey)
    }
}
