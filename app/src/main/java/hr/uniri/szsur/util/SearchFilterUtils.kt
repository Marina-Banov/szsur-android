package hr.uniri.szsur.util

import hr.uniri.szsur.data.model.Filterable
import hr.uniri.szsur.data.model.Survey
import kotlin.collections.ArrayList

fun <T: Filterable> filterByTags(originalList: ArrayList<T>,
                                 tags: ArrayList<String>?): ArrayList<T> {
    if (tags == null || tags.size == 0) {
        return originalList
    }

    val result = ArrayList<T>()
    for (item in originalList) {
        for (tag in tags) {
            if (item.tags.contains(tag)){
                result.add(item)
                break
            }
        }
    }
    return result
}

fun <T: Filterable> search(originalList: ArrayList<T>,
                           query: String?): ArrayList<T> {
    if (query == null || query.isEmpty()) {
        return originalList
    }

    val result = ArrayList<T>()
    for (item in originalList) {
        if (item.title.lowercase().contains(query.lowercase())) {
            result.add(item)
        }
    }
    return result
}

fun <T: Filterable> sortByOrganisation(originalList: ArrayList<T>?, organisation: String?) : ArrayList<T>{
    val firstPart = ArrayList<T>()
    val secondPart = ArrayList<T>()
    if (originalList != null) {
        for (item in originalList) {
            if (item.organisation.equals(organisation)){
                firstPart.add(item)
            }else{
                secondPart.add(item)
            }
        }
    }

    firstPart.addAll(secondPart)
    return firstPart
}

fun filterByStatus(originalList: ArrayList<Survey>, status: Boolean):
        ArrayList<Survey> {
    val result = ArrayList<Survey>()
    for (item in originalList) {
        if (item.published == status) {
            result.add(item)
        }
    }
    return result
}


