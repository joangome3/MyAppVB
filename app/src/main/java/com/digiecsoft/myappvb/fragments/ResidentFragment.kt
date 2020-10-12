package com.digiecsoft.myappvb.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alejandrolora.seccion_07_drawer_recycler_card.listeners.RecyclerResidentListener
import com.digiecsoft.myappvb.R
import com.digiecsoft.myappvb.adapters.ResidentAdapter
import com.digiecsoft.myappvb.models.Residente
import com.digiecsoft.myappvb.toast
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_resident.view.*
import java.util.EventListener
import kotlin.collections.ArrayList


class ResidentFragment : Fragment() {

    private lateinit var _view: View

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var residentDBRef: CollectionReference

    private var residentSubscription: ListenerRegistration? = null

    private val residentsList: ArrayList<Residente> = ArrayList()

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ResidentAdapter

    private val layoutManager by lazy { LinearLayoutManager(context) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_resident, container, false)
        recycler = _view.recyclerViewResident as RecyclerView
        setUpResidentDB()
        setRecyclerView()
        subscribeToResidents()
        return _view
    }

    private fun setUpResidentDB() {
        residentDBRef = store.collection("Residente")
    }

    private fun setRecyclerView() {
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = layoutManager
        adapter = (ResidentAdapter(residentsList, object : RecyclerResidentListener {
            override fun onClick(residente: Residente, position: Int) {
                activity?.toast("Let's go to ${residente.nombres}!")
            }

            override fun onDelete(residente: Residente, position: Int) {
                residentsList.remove(residente)
                adapter.notifyItemRemoved(position)
                activity?.toast("${residente.nombres} has been removed!")
            }
        }))
        recycler.adapter = adapter
    }

    private fun subscribeToResidents() {
        residentSubscription = residentDBRef
            .orderBy("apellidos", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener,
                com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(
                    snapshot: QuerySnapshot?,
                    exception: FirebaseFirestoreException?
                ) {
                    exception?.let {
                        activity!!.toast(resources.getString(R.string.firebase_message_1))
                        return
                    }
                    snapshot?.let {
                        residentsList.clear()
                        val residentes = it.toObjects(Residente::class.java)
                        residentsList.addAll(residentes.asReversed())
                        adapter.notifyDataSetChanged()
                    }
                }
            })
    }

    override fun onDestroyView() {
        residentSubscription?.remove()
        super.onDestroyView()
    }

}