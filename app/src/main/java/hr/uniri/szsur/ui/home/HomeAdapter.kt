package hr.uniri.szsur.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.LayoutCardEventBinding
import hr.uniri.szsur.util.DiffCallback
import hr.uniri.szsur.util.handleClick

class HomeAdapter(private val showDetailsListener: (event: Event) -> Unit,
                  private val sendNotification: () -> Unit) :
        ListAdapter<Event, HomeAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(private var binding: LayoutCardEventBinding):
            RecyclerView.ViewHolder(binding.root) {
        private var userRepository = UserRepository.getInstance(FirebaseFirestore.getInstance())

        fun bind(event: Event, sendNotification: () -> Unit) {
            binding.event = event
            binding.isFavorite = userRepository.user.value!!.favorites.contains(event.documentId)
            binding.favoritesButton.setOnClickListener {
                handleClick(event.documentId, sendNotification)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCardEventBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val event = getItem(position)
        viewHolder.itemView.setOnClickListener { showDetailsListener(event) }
        viewHolder.bind(event, sendNotification)
    }
}
