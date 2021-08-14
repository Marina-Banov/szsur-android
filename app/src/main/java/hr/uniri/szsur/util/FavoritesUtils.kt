package hr.uniri.szsur.util

import hr.uniri.szsur.data.model.FavoriteEntry
import hr.uniri.szsur.data.model.UpdateFavorite
import hr.uniri.szsur.data.repository.UserRepository
import kotlinx.coroutines.*

fun handleClick(id: String, isEvent: Boolean) {
    if (UserRepository.user.value!!.uid == "") {
        // TODO anonymous user
        return
    }

    var liked: Boolean

    (UserRepository.user.value!!.favorites as ArrayList<FavoriteEntry>).apply {
        val favoriteEntry = FavoriteEntry(id, isEvent)
        liked = !contains(favoriteEntry)
        // NOTE optimistic update
        //  Api call returns true if successful and false otherwise
        //  Perhaps revert if failure occurred? Or display a Toast?
        if (liked) { add(favoriteEntry) } else { remove(favoriteEntry) }
    }

    val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    coroutineScope.launch {
        UserRepository.updateFavorites(UpdateFavorite(liked, isEvent, id))
    }
}
