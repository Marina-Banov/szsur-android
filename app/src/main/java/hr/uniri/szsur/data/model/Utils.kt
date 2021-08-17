package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList


@Parcelize
data class FirebaseDate(val _seconds: Long = 0,
                        val _nanoseconds: Long = 0) : Parcelable {
    fun toDate(): Date {
        return Date(this._seconds.times(1000))
    }
}

@Parcelize
data class FavoriteEntry(@Json(name="favoriteId") val id: String = "",
                         val isEvent: Boolean = false) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other !is FavoriteEntry) return false
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

@Parcelize
data class UpdateFavorite(val liked: Boolean,
                          val isEvent: Boolean,
                          val favoriteId: String) : Parcelable


enum class QuestionType(val value: String) {
    SINGLE_CHOICE("single-choice"),
    MULTIPLE_CHOICE("multiple-choice"),
    INPUT_TEXT("input-text")
}
