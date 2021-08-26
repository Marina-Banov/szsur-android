package hr.uniri.szsur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class SurveyAnswer(
    val surveyId: String = "",
    val active: Boolean = false,
    val answers: @RawValue Any? = null
) : Parcelable
