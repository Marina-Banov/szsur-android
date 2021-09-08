package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.google.android.libraries.places.api.model.Place
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Event(
    override val documentId: String = "",
    val description: String = "",
    val image: String = "",
    var location: String = "",
    private var _googlePlace: Place? = null,
    val online: Boolean = false,
    override val organisation: String = "",
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
    val startTime: String = "",
    val endTime: String = "",
    val online: Boolean = false,
    val location: String = "",
    val subscribers: List<String> = listOf(),
    val tags: List<String> = listOf(),
)

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class EventJsonAdapter

class EventAdapter {
    @EventJsonAdapter
    @FromJson
    fun fromJson(json: List<EventJson>): List<Event> {
        return json.map {
            Event(
                it.id,
                it.description,
                it.image,
                it.location,
                null,
                it.online,
                it.organisation,
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
                    .parse(it.startTime) ?: Date(),
                it.tags,
                it.title,
            )
        }
    }

    @ToJson
    fun toJson(@EventJsonAdapter events: List<Event>): List<EventJson> {
        throw UnsupportedOperationException()
    }
}
