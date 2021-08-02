package hr.uniri.szsur.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import hr.uniri.szsur.data.model.User
import hr.uniri.szsur.util.SingletonHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class UserRepository private constructor(db: FirebaseFirestore) {

    private val TAG = "UserRepository"
    private val COLLECTION_NAME = "users"
    private val FAVORITES = "favorites"

    companion object : SingletonHolder<UserRepository, FirebaseFirestore>(::UserRepository)

    private val userDocument = db.collection(COLLECTION_NAME)
                                 .document(Firebase.auth.currentUser!!.uid)
    val user = MutableLiveData<User>()

    init {
        user.value = User()
        userDocument.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.i(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                user.value = snapshot.toObject(User::class.java)
            } else {
                user.value = User()
                Log.i(TAG, "Current data: null")
            }
        }
    }

    suspend fun get() = withContext(Dispatchers.IO) {
        try {
            val res = userDocument.get().await()
            if (res.data == null) User() else res.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            User()
        }
    }

    suspend fun updateFavorites(favorites: List<String>) = withContext(Dispatchers.IO) {
        try {
            val result = userDocument.update(FAVORITES, favorites).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            false
        }
    }
}