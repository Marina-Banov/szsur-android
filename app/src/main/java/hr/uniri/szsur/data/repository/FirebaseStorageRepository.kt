package hr.uniri.szsur.data.repository

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


object FirebaseStorageRepository {

    private const val STORAGE_URL = "gs://szsur-7e723.appspot.com/"
    private val storage = FirebaseStorage.getInstance()

    fun getImageReference(imageURL: String) : StorageReference {
        return storage.getReferenceFromUrl(STORAGE_URL + imageURL)
    }

    fun getImageReferences(imageURLs: List<String>) : List<StorageReference> {
        val refs: MutableList<StorageReference> = mutableListOf()
        for (img in imageURLs) {
            refs.add(storage.getReferenceFromUrl(STORAGE_URL + img))
        }
        return refs
    }

}
