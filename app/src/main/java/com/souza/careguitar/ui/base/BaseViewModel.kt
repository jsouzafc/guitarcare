package com.souza.careguitar.ui.base

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.souza.careguitar.ui.home.Instrument
import com.souza.careguitar.ui.home.Maintenance

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
        instrumentId: String,
        maintenance: Maintenance
    ) {
        val userId = auth.currentUser?.uid

        val reference = db.collection("users").document(userId.orEmpty()).collection("maintenance")

        if (userId != null) {
            reference?.document()?.get()?.addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val documentId = documentSnapshot.id
                    // Use o documentId como a chave do documento
                } else {
                    // O documento não existe
                }
            }

            reference?.add(maintenance)?.addOnSuccessListener {
                getMaintenance(instrumentId)
            }?.addOnFailureListener { exception ->

            }
        }
    }

    fun deleteInstrument(instrument: Instrument) {
        collectionReference?.whereEqualTo("id", instrument.id)?.get()
            ?.addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    // Para cada documento encontrado, exclua-o
                    collectionReference?.document(document.id)?.delete()
                        ?.addOnSuccessListener {
                            // Documento deletado com sucesso
                            _onDeleteInstrument.postValue(Unit)
                        }
                        ?.addOnFailureListener { e ->
                            // Ocorreu um erro ao deletar o documento
                            Log.e("TAG", "Erro ao deletar o documento: $e")
                        }
                }
            }
            ?.addOnFailureListener { e ->
                // Ocorreu um erro ao executar a consulta
                Log.e("TAG", "Erro ao executar a consulta: $e")
            }
    }

    fun updateInstrument(instrumentId: String, name: String) {
        val newInstrumentData = mapOf(
            "name" to name,
        )

        val instrumentRef = collectionReference?.document(instrumentId)

        instrumentRef
            ?.update(newInstrumentData)
            ?.addOnSuccessListener {
                // Instrumento foi atualizado com sucesso
            }
            ?.addOnFailureListener { e ->
                // Ocorreu um erro ao tentar atualizar o instrumento, lide com o erro aqui
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

            /*    for (document in querySnapshot.documents) {
                    val instrument = document.toObject(Instrument::class.java)
                    if (instrument != null) {
                        // Atualize o instrument encontrado, se necessário.
                        // Por exemplo, para atualizar a imagem:
                        instrument.image = image

                        // Atualize o documento no Firestore.
                        collectionReference?.document(document.id)?.set(instrument)
                            ?.addOnSuccessListener {
                                query?.get()
                                    ?.addOnSuccessListener { querySnapshot ->
                                        if (!querySnapshot.isEmpty) {
                                            val instrument = querySnapshot.documents[0].toObject(Instrument::class.java)
                                            val image = instrument?.image ?: ""
                                            _instrumentImage.postValue(image)
                                        }
                                    }
                            }
                            ?.addOnFailureListener { e ->
                                // Ocorreu um erro na atualização.
                            }
                    }
                }*/
            }
            ?.addOnFailureListener { e ->
                // Ocorreu um erro ao buscar documentos.
            }

    }

    private fun getHelp() {
        val query = db.collection("help")

        query.get().addOnSuccessListener {
            val urls = ArrayList<String>()

            for (document in it.documents) {
                val url = document.getString("url")
                url?.let { urls.add(it) }
            }

            _help.postValue(urls)
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