package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User (
    @Json(name="id") var uid: String = "",
    var isAdmin: Boolean = false,
    var email: String = "",
    var favorites: List<FavoriteEntry> = ArrayList(),
    var solvedSurveys: List<String> = ArrayList(),
) : Parcelable
