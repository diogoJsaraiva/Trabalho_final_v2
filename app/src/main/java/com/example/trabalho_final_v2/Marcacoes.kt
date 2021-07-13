package com.example.trabalho_final_v2

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import java.util.*

data class Marcacoes (var id: Long = -1, var data: Date, var idUtentes: Long, var dose: Int){
    fun toContentValues(): ContentValues {
        val valores = ContentValues().apply {
            put(TabelaMarcacoes.CAMPO_DATA, data.time)
            put(TabelaMarcacoes.CAMPO_ID_UTENTES, idUtentes)
            put(TabelaMarcacoes.CAMPO_DOSE, dose)
        }

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor): Marcacoes {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colData = cursor.getColumnIndex(TabelaMarcacoes.CAMPO_DATA)

            val colIdUtentes= cursor.getColumnIndex(TabelaMarcacoes.CAMPO_ID_UTENTES)
            val colIdDose = cursor.getColumnIndex(TabelaMarcacoes.CAMPO_DOSE)

            val id = cursor.getLong(colId)
            val data = cursor.getLong(colData)
            val idUtentes = cursor.getLong(colIdUtentes)
            val Dose = cursor.getInt(colIdDose)

            return Marcacoes(id, Date(data), idUtentes, Dose)
        }
    }
}