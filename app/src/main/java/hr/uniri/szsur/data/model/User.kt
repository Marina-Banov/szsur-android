package hr.uniri.szsur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.RawValue

@Parcelize
data class User (
    @DocumentId
    var uid: String = "",
    var isAdmin: Boolean = false,
    var email: String = "",
    var favorites: ArrayList<String> = ArrayList(),
    var solved_surveys: ArrayList<String> = ArrayList(),

) : Parcelable