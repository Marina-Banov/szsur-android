package hr.uniri.szsur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.squareup.moshi.Json

@Parcelize
data class User (
    @Json(name="id") var uid: String = "",
    var isAdmin: Boolean = false,
    var email: String = "",
    var favorites: List<String> = ArrayList(),
    var solved_surveys: List<String> = ArrayList(),
) : Parcelable
