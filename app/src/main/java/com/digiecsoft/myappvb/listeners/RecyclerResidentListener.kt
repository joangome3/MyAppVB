package com.alejandrolora.seccion_07_drawer_recycler_card.listeners

import com.digiecsoft.myappvb.models.Residente

interface RecyclerResidentListener {

    fun onClick(residente: Residente, position: Int)

    fun onDelete(residente: Residente, position: Int)

}