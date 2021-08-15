package hr.uniri.szsur.data.model

import com.squareup.moshi.JsonClass


interface Enums { val values: List<*> }

@JsonClass(generateAdapter = true)
data class Tags(override val values: List<String> = ArrayList()): Enums
