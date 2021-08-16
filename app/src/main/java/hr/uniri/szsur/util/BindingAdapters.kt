package hr.uniri.szsur.util

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import hr.uniri.szsur.R
import hr.uniri.szsur.data.FirestoreRepository
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.data.model.Filterable
import hr.uniri.szsur.data.model.Survey
import hr.uniri.szsur.ui.favorites.FavoritesAdapter
import hr.uniri.szsur.ui.home.HomeAdapter
import hr.uniri.szsur.ui.survey_list.SurveyListAdapter


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {
    val storageReference = FirestoreRepository().getImageReference(url)
    GlideApp.with(view.context).load(storageReference).into(view)
}

@JvmName("eventListData")
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Event>?) {
    val adapter = recyclerView.adapter as HomeAdapter
    adapter.submitList(data)
}

@JvmName("surveyListData")
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Survey>?) {
    val adapter = recyclerView.adapter as SurveyListAdapter
    adapter.submitList(data)
}

@JvmName("favoritesListData")
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Filterable>?) {
    val adapter = recyclerView.adapter as FavoritesAdapter
    adapter.submitList(data)
}


@BindingAdapter("surveyStatusIcon")
fun setStatusIcon(view: ImageView, item: Survey?) {
    view.setImageResource(when (item!!.published) {
        true -> R.drawable.ic_check
        false -> R.drawable.ic_help
    })
}

@BindingAdapter("favoriteIcon")
fun setFavoriteIcon(view: View, isFavorite: Boolean) {
    val icon = if (isFavorite) { R.drawable.ic_favorite_filled }
               else { R.drawable.ic_favorite }
    if (view is ImageButton) {
        view.setImageResource(icon)
    } else if (view is MaterialButton) {
        view.setIconResource(icon)
    }
}

@BindingAdapter("textLocation")
fun setLocationText(view: TextView, event: Event) {
    view.text = if (event.online) {
        view.context.getString(R.string.event_online)
    } else {
        event.googlePlace?.name
    }
}

@BindingAdapter("textLocationDetails")
fun setLocationDetails(view: TextView, event: Event) {
    view.text = if (event.online) event.location else event.googlePlace?.address
}

@BindingAdapter("locationIcon")
fun setLocationIcon(view: ImageView, event: Event) {
    view.setImageResource(when (event.online) {
        true -> R.drawable.ic_globe
        false -> R.drawable.ic_place
    })
}
