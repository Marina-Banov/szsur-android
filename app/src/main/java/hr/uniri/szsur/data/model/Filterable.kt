package hr.uniri.szsur.data.model


interface Filterable {
    val documentId: String
    val tags: List<String>
    val title: String
    val organisation: String
}
