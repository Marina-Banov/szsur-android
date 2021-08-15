package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User (
    val uid: String = "",
    val isAdmin: Boolean = false,
    val email: String = "",
    var favorites: List<String> = ArrayList(),
    val solvedSurveys: List<String> = ArrayList(),
) : Parcelable

@JsonClass(generateAdapter = true)
data class UserJson (
    val isAdmin: Boolean = false,
    val email: String = "",
    val favorites: List<FavoriteEntry> = ArrayList(),
    val solvedSurveys: List<String> = ArrayList(),
)
