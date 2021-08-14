package hr.uniri.szsur.data.model


interface Enums { val values: List<*> }

data class Tags(override val values: List<String> = ArrayList()): Enums
