package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Survey (
    @Json(name="id") override val documentId: String = "",
    override val title: String = "",
    val description: String = "",
    val image: String = "",
    val published: Boolean = false,
    val resultDescription: String = "",
    val resultImages: List<String> = listOf(),
    val active: Boolean = false,
    val activeQuestion: String = "",
    val activeQuestionChoices: List<String> = listOf(),
    override val organisation: String = "",
    var questions: List<Question>? = listOf(),
    var results: Map<String, Int>? = mapOf(),
    override val tags: List<String> = listOf(),
) : Parcelable, Filterable
