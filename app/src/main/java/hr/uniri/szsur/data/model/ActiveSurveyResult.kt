package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ActiveSurveyResult (
    @Json(name="id") val documentId: String = "",
    val q: String = "",
) : Parcelable
