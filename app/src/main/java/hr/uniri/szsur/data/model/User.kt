package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import hr.uniri.szsur.data.repository.UserRepository
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User (
    val uid: String = "",
    val isAdmin: Boolean = false,
    val email: String = "",
    var favorites: List<String> = ArrayList(),
    val solvedSurveys: List<String> = ArrayList(),
    val organisation: String = "",
) : Parcelable

@JsonClass(generateAdapter = true)
data class UserJson (
    val isAdmin: Boolean = false,
    val email: String = "",
    val favorites: List<FavoriteEntry> = ArrayList(),
    val solvedSurveys: List<String> = ArrayList(),
    val organisation: String = "",
) {
    fun getUserFromJson(): User {
        val favorites = ArrayList<String>()
        for (f in this.favorites) {
            favorites.add(f.id)
        }

        return User(
            UserRepository.uid,
            isAdmin,
            email,
            favorites,
            solvedSurveys,
            organisation,
        )
    }
}
