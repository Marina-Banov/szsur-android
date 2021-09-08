package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Question (
    @Json(name="id") val documentId: String = "",
    val order: String = "",
    val type: String = "",
    val question: String = "",
    val required: Boolean = true,
    val choices: List<String> = listOf(),
) : Parcelable {
    override fun toString(): String {
        var s = "$order. $question"
        if (required) {
            s = "* $s"
        }
        return s
    }
}
