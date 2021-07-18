package com.example.trabalho_final_v2

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import java.util.*


data class Utentes(var id: Long = -1, var nome: String, var telefone: String, var contribuinte: Int, var dataNascimento: Date, var dose: Int , var id_vacina : Long,var nomeVacina: String?=null) {
    fun toContentValues(): ContentValues {
        val valores = ContentValues().apply {
            put(TabelaUtentes.CAMPO_NOME, nome)
            put(TabelaUtentes.CAMPO_TELEFONE, telefone)
            put(TabelaUtentes.CAMPO_CONTRIBUINTE, contribuinte)
            put(TabelaUtentes.CAMPO_DATA_NASCIMENTO, dataNascimento.time)
            put(TabelaUtentes.CAMPO_DOSE, dose)
            put(TabelaUtentes.CAMPO_ID_VACINAS, id_vacina.toLong())
        }

        return valores
    }

    companion object {
        fun fromCursor(cursor: Cursor): Utentes {
            val colId = cursor.getColumnIndex(BaseColumns._ID)
            val colNome = cursor.getColumnIndex(TabelaUtentes.CAMPO_NOME)
            val colTelefone = cursor.getColumnIndex(TabelaUtentes.CAMPO_TELEFONE)
            val colContribuinte = cursor.getColumnIndex(TabelaUtentes.CAMPO_CONTRIBUINTE)
            val colDataNascimento = cursor.getColumnIndex(TabelaUtentes.CAMPO_DATA_NASCIMENTO)
            val colDose = cursor.getColumnIndex(TabelaUtentes.CAMPO_DOSE)
            val colVacina = cursor.getColumnIndex(TabelaUtentes.CAMPO_ID_VACINAS)
            val colNomeVacina = cursor.getColumnIndex(TabelaUtentes.CAMPO_EXTERNO_NOME_VACINA)

            val id = cursor.getLong(colId)
            val nome = cursor.getString(colNome)
            val telefone = cursor.getString(colTelefone)
            val contribuinte = cursor.getInt(colContribuinte)
            val dataNascimento = cursor.getLong(colDataNascimento)
            val dose = cursor.getInt(colDose)
            val idvacina = cursor.getLong(colVacina)
            val nomevacina = if (colNomeVacina != -1) cursor.getString(colNomeVacina) else null

            return Utentes(id, nome, telefone, contribuinte, Date(dataNascimento), dose,idvacina,nomevacina)
        }
    }
}