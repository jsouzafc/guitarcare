package com.souza.careguitar.ui.home

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
): ViewModel() {

    private val _instruments = MutableLiveData<List<Instrument>>()
    val instruments: LiveData<List<Instrument>> = _instruments
    var collectionReference: CollectionReference? = null

    init {
        collectionReference = auth.currentUser?.uid?.let {
            db.collection("users").document(it).collection("instruments")
        }
    }

    fun getInstruments() {
        collectionReference?.get()?.addOnSuccessListener {
            val instruments = it.toObjects(Instrument::class.java)
            _instruments.postValue(instruments)
        }

    }

    fun addInstrument(instrument: Instrument) {
        collectionReference?.add(instrument)
            ?.addOnSuccessListener { documentReference ->
                // Sucesso, o objeto foi adicionado com sucesso e você pode acessar o ID do documento, se necessário
                val documentId = documentReference.id
            }
            ?.addOnFailureListener { e ->
                val test = 1
                // Falha ao adicionar o objeto à coleção, lide com o erro aqui
            }
    }

    fun deleteInstrument(instrument: Instrument) {
        instrument.id?.let {
            collectionReference?.document(it)?.delete()
                ?.addOnSuccessListener { documentReference ->
                    // Sucesso, o objeto foi adicionado com sucesso e você pode acessar o ID do documento, se necessário
                }
                ?.addOnFailureListener { e ->
                    val test = 1
                    // Falha ao adicionar o objeto à coleção, lide com o erro aqui
                }
        }
    }

    fun updateInstrument(instrumentId: String, name: String) {
        val newInstrumentData = mapOf(
            "name" to name,
        )

        val db = FirebaseFirestore.getInstance()
        val instrumentsCollection = db.collection("instrumentos")
        val instrumentRef = instrumentsCollection.document(instrumentId)

        instrumentRef
            .update(newInstrumentData)
            .addOnSuccessListener {
                // Instrumento foi atualizado com sucesso
            }
            .addOnFailureListener { e ->
                // Ocorreu um erro ao tentar atualizar o instrumento, lide com o erro aqui
            }
    }
}