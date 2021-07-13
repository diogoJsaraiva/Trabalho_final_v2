package com.example.trabalho_final_v2

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaMarcacoes(db: SQLiteDatabase) : BaseColumns {
    private val db : SQLiteDatabase = db
    fun cria() = db?.execSQL(
        "CREATE TABLE $NOME_TABELA (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, $CAMPO_DATA INTEGER NOT NULL, $CAMPO_DOSE INTEGER NOT NULL,$CAMPO_ID_UTENTES INTEGER NOT NULL, FOREIGN KEY ($CAMPO_ID_UTENTES) REFERENCES ${TabelaUtentes.NOME_TABELA})"
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
        return db.query(NOME_TABELA, columns, selection, selectionArgs, groupBy, having, orderBy)
    }

    companion object{
        const val NOME_TABELA = "Marcacao"
        const val CAMPO_DATA = "data_administracao"
        const val CAMPO_DOSE = "dose"
        const val CAMPO_ID_UTENTES = "id_pessoa"



        val TODOS_CAMPOS =arrayOf(BaseColumns._ID, CAMPO_DATA,CAMPO_ID_UTENTES)

    }

}