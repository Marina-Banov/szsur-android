package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.google.android.libraries.places.api.model.Place
import com.squareup.moshi.JsonClass
import java.util.Date
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Event(
    override val documentId: String = "",
    val description: String = "",
    val image: String = "",
    var location: String = "",
    private var _googlePlace: Place? = null,
    val online: Boolean = false,
    val organisation: String = "",
    val startTime: Date = Date(),
    override val tags: List<String> = listOf(),
    override val title: String = "",
) : Parcelable, Filterable {
    var googlePlace: Place?
        get() = _googlePlace
        set(place) {
            setOnsiteLocation(place)
        }

    fun setOnsiteLocation(place: Place?) {
        this._googlePlace = place
        this.location = ""
    }
}

@JsonClass(generateAdapter = true)
data class EventJson (
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val organisation: String = "",
    val image: String = "",
    val startTime: FirebaseDate = FirebaseDate(),
    val endTime: FirebaseDate = FirebaseDate(),
    val online: Boolean = false,
    val location: String = "",
    val subscribers: List<String> = listOf(),
    val tags: List<String> = listOf(),
) {
    fun getEventFromJson(): Event {
        return Event(
            id,
            description,
            image,
            location,
            null,
            online,
            organisation,
            startTime.toDate(),
            tags,
            title,
        )
    }
}
