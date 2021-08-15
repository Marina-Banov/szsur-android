package hr.uniri.szsur.util

import hr.uniri.szsur.data.model.UpdateFavorite
import hr.uniri.szsur.data.repository.UserRepository
import kotlinx.coroutines.*

fun handleClick(id: String, isEvent: Boolean) {
    if (!UserRepository.isUserRegistered()) {
        // TODO anonymous user
        return
    }

    var liked: Boolean

    (UserRepository.user.value!!.favorites as ArrayList<String>).apply {
        liked = !contains(id)
        // NOTE optimistic update
        //  Api call returns true if successful and false otherwise
        //  Perhaps revert if failure occurred? Or display a Toast?
        if (liked) { add(id) } else { remove(id) }
        // Live data is not updated simply by updating the ArrayList
        // Must also update the reference
        UserRepository.user.value = UserRepository.user.value
    }

    val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    coroutineScope.launch {
        UserRepository.updateFavorites(UpdateFavorite(liked, isEvent, id))
    }
}
