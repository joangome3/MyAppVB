package com.digiecsoft.myappvb.models

data class Residente(
    val tipo_documento: String? = null,
    val numero_documento: String? = null,
    val nombres: String? = null,
    val apellidos: String? = null,
    val manzana: Int? = null,
    val villa: Int? = null,
    val tipo_telefono: String? = null,
    val numero_telefono: String? = null,
    val esta_al_dia_en_alicuotas: String? = null
) {}