package com.example.trabalho_final_v2

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class Vacinas (var id: Long = -1, var nomeVacina: String,var lote : Int) {
    fun toContentValues(): ContentValues {
        val valores = ContentValues().apply {
            put(TabelaVacinas.CAMPO_NOME_VACINA, nomeVacina)
            put(TabelaVacinas.CAMPO_LOTE, lote)

        }

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor): Vacinas {
            val colId = cursor.getColumnIndex(BaseColumns._ID)

            val colNomeVacina = cursor.getColumnIndex(TabelaVacinas.CAMPO_NOME_VACINA)
            val colNomeLote = cursor.getColumnIndex(TabelaVacinas.CAMPO_LOTE)

            val id = cursor.getLong(colId)

            val nomeVacina = cursor.getString(colNomeVacina)
            val lote = cursor.getInt(colNomeLote)

            return Vacinas(id, nomeVacina,lote)
        }
    }
}