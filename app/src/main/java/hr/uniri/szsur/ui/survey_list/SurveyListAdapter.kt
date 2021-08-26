package hr.uniri.szsur.ui.survey_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.LayoutCardSurveyBinding
import hr.uniri.szsur.util.DiffCallback
import hr.uniri.szsur.util.handleClick

class SurveyListAdapter(private val showDetailsListener: (survey: Survey) -> Unit) :
        ListAdapter<Survey, SurveyListAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(private var binding: LayoutCardSurveyBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(survey: Survey) {
            binding.survey = survey
            binding.isFavorite = UserRepository.user.value!!.favorites.contains(survey.documentId)
            binding.favoritesButton.setOnClickListener {
                handleClick(survey.documentId, false)
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
