package hr.uniri.szsur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Organization(
    val description: String = "",
    val name: String = "",
    val contacts: Contacts = Contacts(),
) : Parcelable
