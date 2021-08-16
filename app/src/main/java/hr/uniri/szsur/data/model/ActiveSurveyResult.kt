package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActiveSurveyResult (
    @DocumentId
    val documentId: String = "",
    val q: String = "",
) : Parcelable