package com.souza.careguitar.ui.base

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.souza.careguitar.ui.home.Instrument
import com.souza.careguitar.ui.home.Maintenance
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

class BaseViewModel(
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
): ViewModel() {

    private val _instruments = MutableLiveData<List<Instrument>>()
    val instruments: LiveData<List<Instrument>> = _instruments

    private val _instrumentImage = MutableLiveData<String>()
    val instrumentImage: LiveData<String> = _instrumentImage

    private val _onDeleteInstrument = MutableLiveData<Unit>()
    val onDeleteInstrument: LiveData<Unit> = _onDeleteInstrument

    private val _onInstrumentAdded = MutableLiveData<Unit>()
    val onInstrumentAdded: LiveData<Unit> = _onInstrumentAdded

    private val _onMaintenanceAdded = MutableLiveData<Unit>()
    val onMaintenanceAdded: LiveData<Unit> = _onMaintenanceAdded

    var collectionReference: CollectionReference? = null

    private val _maintenance = MutableLiveData<List<Maintenance>>()
    val maintenance: LiveData<List<Maintenance>> = _maintenance

    private val _help = MutableLiveData<List<String>>()
    val help: LiveData<List<String>> = _help

    init {
        collectionReference = auth.currentUser?.uid?.let {
            db.collection("users").document(it).collection("instruments")
        }
        getHelp()
    }

    fun getInstruments(query: String = "") {
        collectionReference?.get()?.addOnSuccessListener {
            val instruments = it.toObjects(Instrument::class.java)
            if (query.isNotBlank())
                _instruments.postValue(instruments.filter { it.name?.contains(query, true) == true })
            else _instruments.postValue(instruments)
        }
    }

    fun getMaintenance(id: String) {
        val query = auth.currentUser?.uid?.let {
            db.collection("users").document(it).collection("maintenance")
        }?.whereEqualTo("instrumentId", id)

        query?.get()?.addOnSuccessListener {
            val maintenance = it.toObjects(Maintenance::class.java)
            _maintenance.postValue(maintenance)
        }
    }

    fun addInstrument(instrument: Instrument) {
        collectionReference?.add(instrument)
            ?.addOnSuccessListener { documentReference ->
                _onInstrumentAdded.postValue(Unit)
            }
            ?.addOnFailureListener { e -> }
    }

    fun addMaintenance(
        maintenance: Maintenance
    ) {
        val userId = auth.currentUser?.uid

        val reference = db.collection("users").document(userId.orEmpty()).collection("maintenance")

        if (userId != null) {
            reference?.add(maintenance)?.addOnSuccessListener {
                _onMaintenanceAdded.postValue(Unit)
            }?.addOnFailureListener { exception ->

            }
        }
    }

    fun deleteInstrument(instrument: Instrument) {
        collectionReference?.whereEqualTo("id", instrument.id)?.get()
            ?.addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    collectionReference?.document(document.id)?.delete()
                        ?.addOnSuccessListener {
                            _onDeleteInstrument.postValue(Unit)
                        }
                        ?.addOnFailureListener { e ->
                            Log.e("TAG", "Erro ao deletar o documento: $e")
                        }
                }
            }
            ?.addOnFailureListener { e ->
                Log.e("TAG", "Erro ao executar a consulta: $e")
            }
    }

    fun updateInstrumentImage(instrumentId: String, image: String) {
        val query = collectionReference?.document()

        query?.get()
            ?.addOnSuccessListener { querySnapshot ->
                if (querySnapshot != null) {
                    querySnapshot.data?.let { data ->
                        data.forEach { (key, _) ->
                            Log.d("TAG", key)
                        }
                    }
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            ?.addOnFailureListener { e ->
            }
    }

    private fun getHelp() {
        val reference = storage.reference
        val imageRef = reference.child("help/help.json")

        val localFile = File.createTempFile("temp", "json")

        val storageTask: StorageTask<FileDownloadTask.TaskSnapshot> = imageRef.getFile(localFile)

        storageTask.addOnSuccessListener {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(List::class.java, HelpItem::class.java)
            val adapter = moshi.adapter<List<HelpItem>>(listType)
            val helpItems: List<HelpItem>? = adapter.fromJson(localFile.readText())
             _help.postValue(helpItems?.map { it.url }?.filterNotNull())
        }.addOnFailureListener {
            println("Erro ao baixar o arquivo JSON.")
        }
    }

    fun uploadInstrumentImage(
        instrumentId: String,
        selectedImageUri: Uri
    ) {
        val userId = auth.currentUser?.uid
        val reference = storage.reference
        val imageRef = reference.child("user_images/$userId/$instrumentId/$selectedImageUri")

        imageRef.putFile(selectedImageUri)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    updateInstrumentImage(instrumentId, imageUrl)
                }
            }
            .addOnFailureListener { e -> }
    }
}

data class HelpItem(
    val url: String?
)