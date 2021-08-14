package hr.uniri.szsur.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.data.model.Filterable
import hr.uniri.szsur.data.model.SurveyModel
import hr.uniri.szsur.databinding.LayoutCardEventBinding
import hr.uniri.szsur.databinding.LayoutCardSurveyBinding
import hr.uniri.szsur.ui.home.HomeAdapter
import hr.uniri.szsur.ui.survey_list.SurveyListAdapter
import hr.uniri.szsur.util.DiffCallback

class  FavoritesAdapter(private val showEventDetailsListener: (event: Event) -> Unit,
                        private val showSurveyDetailsListener: (survey: SurveyModel) -> Unit) :
    ListAdapter<Filterable, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val EVENT = 0
        private const val SURVEY = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is Event) EVENT else SURVEY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EVENT) {
            val binding = LayoutCardEventBinding.inflate(LayoutInflater.from(parent.context))
            HomeAdapter.ViewHolder(binding)
        } else {
            val binding = LayoutCardSurveyBinding.inflate(LayoutInflater.from(parent.context))
            SurveyListAdapter.ViewHolder(binding)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.apply {
            if (this is HomeAdapter.ViewHolder) {
                itemView.setOnClickListener { showEventDetailsListener(item as Event) }
                bind(item as Event)
            } else if (this is SurveyListAdapter.ViewHolder) {
                itemView.setOnClickListener { showSurveyDetailsListener(item as SurveyModel) }
                bind(item as SurveyModel)
            }
        }
    }
}
