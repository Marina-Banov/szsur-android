package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Event(
    val description: String = "",
    val image: String = "",
    val location: String = "",
    var googlePlace: Place? = null,
    val online: Boolean = false,
    val organisation: String = "",
    val startTime: Date = Date(),
    override val tags: List<String> = listOf(),
    override val title: String = "",
    override val documentId: String = "",
) : Parcelable, Filterable {
    constructor(e: HurrDurrEvent) : this(
        e.description,
        e.image,
        if (e.online) e.location else "",
        null,
        e.online,
        e.organisation,
        e.startTime?.toDate() ?: Date(),
        e.tags,
        e.title,
        e.id,
    )
}

data class HurrDurrEvent(
    val id: String = "",
    val description: String = "",
    val endTime: Timestamp? = null,
    val image: String = "",
    val location: String = "",
    val online: Boolean = false,
    val organisation: String = "",
    val startTime: Timestamp? = null,
    val subscribers: List<String> = listOf(),
    val tags: List<String> = listOf(),
    val title: String = "",
)
