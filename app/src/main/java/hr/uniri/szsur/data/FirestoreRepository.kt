package hr.uniri.szsur.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreRepository {

    val TAG = "FIRESTORE_REPOSITORY"
    private val storageURL = "gs://szsur-7a759.appspot.com/"
    var firestoreDB = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    /** Get published surveys */
    fun getPublishedSurveys(): Query {
        return firestoreDB.collection("surveys").whereEqualTo(
            "published",
            true
        )
    }

    /** Get unpublished surveys */
    fun getUnpublishedSurveys() : Query {
        return firestoreDB.collection("surveys").whereEqualTo(
            "published",
            false
        )
    }

    /** Get image reference */
    fun getImageReference(imageURL: String) : StorageReference {
        return storage.getReferenceFromUrl(storageURL + imageURL)
    }

    /** Get array of image references */
    fun getImageReferences(imageURLs: List<String>) : List<StorageReference> {
        val refs : MutableList<StorageReference> = mutableListOf()
        for (img in imageURLs) {
            refs.add(storage.getReferenceFromUrl(storageURL + img))
        }
        return refs
    }


}