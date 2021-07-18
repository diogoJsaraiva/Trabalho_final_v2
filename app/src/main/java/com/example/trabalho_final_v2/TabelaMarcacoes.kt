package com.example.trabalho_final_v2

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaMarcacoes(db: SQLiteDatabase) : BaseColumns {
    private val db : SQLiteDatabase = db
    fun cria() = db?.execSQL(
        "CREATE TABLE $NOME_TABELA (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, $CAMPO_DATA INTEGER NOT NULL,  $CAMPO_DOSE INTEGER NOT NULL,$CAMPO_ID_UTENTES INTEGER NOT NULL, FOREIGN KEY ($CAMPO_ID_UTENTES) REFERENCES ${TabelaUtentes.NOME_TABELA})"
    )

    fun insert(values: ContentValues): Long {
        return db.insert(NOME_TABELA, null, values)
    }

    fun update(values: ContentValues, whereClause: String, whereArgs: Array<String>): Int {
        return db.update(NOME_TABELA, values, whereClause, whereArgs)
    }

    fun delete(whereClause: String, whereArgs: Array<String>): Int {
        return db.delete(NOME_TABELA, whereClause, whereArgs)
    }

    fun query(
        columns: Array<String>,
        selection: String?,
        selectionArgs: Array<String>?,
        groupBy: String?,
        having: String?,
        orderBy: String?
    ): Cursor? {
        val ultimaColuna = columns.size - 1

        var posColNomeCategoria = -1 // -1 indica que a coluna não foi pedida
        for (i in 0..ultimaColuna) {
            if (columns[i] == CAMPO_EXTERNO_NOME_UTENTE) {
                posColNomeCategoria = i
                break
            }
        }

        if (posColNomeCategoria == -1) {
            return db.query(TabelaUtentes.NOME_TABELA, columns, selection, selectionArgs, groupBy, having, orderBy)
        }

        var colunas = ""
        for (i in 0..ultimaColuna) {
            if (i > 0) colunas += ","

            colunas += if (i == posColNomeCategoria) {
                "${TabelaUtentes.NOME_TABELA}.${TabelaUtentes.CAMPO_NOME} AS ${CAMPO_EXTERNO_NOME_UTENTE}"
            } else {
                "$NOME_TABELA.${columns[i]}"
            }
        }

        val tabelas = "$NOME_TABELA INNER JOIN ${TabelaUtentes.NOME_TABELA} ON ${TabelaUtentes.NOME_TABELA}.${BaseColumns._ID}=${CAMPO_ID_UTENTES}"

        var sql = "SELECT $colunas FROM $tabelas"

        if (selection != null) sql += " WHERE $selection"

        if (groupBy != null) {
            sql += " GROUP BY $groupBy"
            if (having != null) " HAVING $having"
        }

        if (orderBy != null) sql += " ORDER BY ${NOME_TABELA}.$orderBy"

        return db.rawQuery(sql, selectionArgs)
    }

    companion object{
        const val NOME_TABELA = "Marcacao"
        const val CAMPO_DATA = "data_administracao"
        const val CAMPO_DOSE = "dose"
        const val CAMPO_ID_UTENTES = "id_pessoa"
        const val CAMPO_EXTERNO_NOME_UTENTE = "nomeutente"


        val TODOS_CAMPOS =arrayOf(BaseColumns._ID, CAMPO_DATA,CAMPO_ID_UTENTES, CAMPO_DOSE,CAMPO_EXTERNO_NOME_UTENTE)

    }

}