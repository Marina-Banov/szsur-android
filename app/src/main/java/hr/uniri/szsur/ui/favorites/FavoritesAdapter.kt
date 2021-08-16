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
import java.lang.IllegalArgumentException

class  FavoritesAdapter(private val showEventDetailsListener: (event: Event) -> Unit,
                        private val showSurveyDetailsListener: (survey: SurveyModel) -> Unit) :
    ListAdapter<Filterable, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val EVENT = 0
        private const val SURVEY = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is Event -> EVENT
            is SurveyModel -> SURVEY
            else -> throw IllegalArgumentException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EVENT -> {
                val binding = LayoutCardEventBinding.inflate(LayoutInflater.from(parent.context))
                HomeAdapter.ViewHolder(binding)
            }
            SURVEY -> {
                val binding = LayoutCardSurveyBinding.inflate(LayoutInflater.from(parent.context))
                SurveyListAdapter.ViewHolder(binding)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (viewHolder) {
            is HomeAdapter.ViewHolder -> {
                viewHolder.itemView.setOnClickListener {
                    showEventDetailsListener(item as Event)
                }
                viewHolder.bind(item as Event)
            }
            is SurveyListAdapter.ViewHolder -> {
                viewHolder.itemView.setOnClickListener {
                    showSurveyDetailsListener(item as SurveyModel)
                }
                viewHolder.bind(item as SurveyModel)
            }
            else -> throw IllegalArgumentException()
        }
    }
}
