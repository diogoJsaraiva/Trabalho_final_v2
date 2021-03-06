package com.example.trabalho_final_v2

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

data class Vacinas (var id: Long = -1, var nomevacina: String) {
    fun toContentValues(): ContentValues {
        val valores = ContentValues().apply {
            put(TabelaVacinas.CAMPO_NOME, nomevacina)


        }

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor): Vacinas {
            val colId = cursor.getColumnIndex(BaseColumns._ID)

            val colNomeVacina = cursor.getColumnIndex(TabelaVacinas.CAMPO_NOME)


            val id = cursor.getLong(colId)

            val nomevacina = cursor.getString(colNomeVacina)


            return Vacinas(id, nomevacina)
        }
    }
}