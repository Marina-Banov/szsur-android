package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(
        @DocumentId
        val documentId: String = "",
        val order: String = "",
        val type: String = "",
        val question: String = "",
        val choices: List<String> = listOf()
) : Parcelable {
        override fun toString(): String {
                return order + ". " + question
        }
}