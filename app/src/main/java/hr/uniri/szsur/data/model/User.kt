package hr.uniri.szsur.data.model

import android.os.Parcelable
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import hr.uniri.szsur.data.repository.UserRepository
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User (
    val uid: String = "",
    val email: String = "",
    var favorites: List<String> = ArrayList(),
    val solvedSurveys: List<String> = ArrayList(),
    val organisation: String = "",
) : Parcelable

@JsonClass(generateAdapter = true)
data class UserJson (
    val email: String = "",
    val favorites: List<FavoriteEntry> = ArrayList(),
    val solvedSurveys: List<String> = ArrayList(),
    val organisation: String = "",
)
        

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class UserJsonAdapter

class UserAdapter {
    @UserJsonAdapter
    @FromJson
    fun fromJson(json: UserJson): User {
        return User(
            UserRepository.uid,
            json.email,
            json.favorites.map{ it.id },
            json.solvedSurveys,
            json.organisation,

        )
    }

    @ToJson
    fun toJson(@UserJsonAdapter user: User): UserJson {
        throw UnsupportedOperationException()
    }
}
