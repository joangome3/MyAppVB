package com.digiecsoft.myappvb.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alejandrolora.seccion_07_drawer_recycler_card.listeners.RecyclerResidentListener
import com.digiecsoft.myappvb.inflate
import com.digiecsoft.myappvb.models.Residente
import com.digiecsoft.myappvb.R
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class ResidentAdapter(
    private val residentes: List<Residente>,
    private val listener: RecyclerResidentListener
) : RecyclerView.Adapter<ResidentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.recycler_view_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(residentes[position], listener)

    override fun getItemCount() = residentes.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(residente: Residente, listener: RecyclerResidentListener) = with(itemView) {
            textViewResidentFullName.text = residente.nombres + " " + residente.apellidos
            textViewResidentManzana.text = "Mz: " + residente.manzana + ", V: " + residente.villa
            textViewResidentDocument.text = residente.numero_documento
            textViewResidentPhone.text = residente.numero_telefono
            // Clicks Events
            setOnClickListener { listener.onClick(residente, adapterPosition) }
            buttonDelete.setOnClickListener { listener.onDelete(residente, adapterPosition) }
        }
    }
}