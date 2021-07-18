package com.example.trabalho_final_v2

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaUtentes (db: SQLiteDatabase) : BaseColumns {
    private val db : SQLiteDatabase = db
    fun cria() = db?.execSQL("CREATE TABLE $NOME_TABELA (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, $CAMPO_NOME TEXT NOT NULL, $CAMPO_TELEFONE TEXT NOT NULL ,$CAMPO_CONTRIBUINTE NUMBER NOT NULL, $CAMPO_DATA_NASCIMENTO INTEGER NOT NULL, $CAMPO_DOSE INTEGER NOT NULL,$CAMPO_ID_VACINAS INTEGER NOT NULL, FOREIGN KEY($CAMPO_ID_VACINAS) REFERENCES ${TabelaVacinas.NOME_TABELA})"
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
            if (columns[i] == CAMPO_EXTERNO_NOME_VACINA) {
                posColNomeCategoria = i
                break
            }
        }

        if (posColNomeCategoria == -1) {
            return db.query(NOME_TABELA, columns, selection, selectionArgs, groupBy, having, orderBy)
        }

        var colunas = ""
        for (i in 0..ultimaColuna) {
            if (i > 0) colunas += ","

            colunas += if (i == posColNomeCategoria) {
                "${TabelaVacinas.NOME_TABELA}.${TabelaVacinas.CAMPO_NOME} AS $CAMPO_EXTERNO_NOME_VACINA"
            } else {
                "${NOME_TABELA}.${columns[i]}"
            }
        }

        val tabelas = "$NOME_TABELA INNER JOIN ${TabelaVacinas.NOME_TABELA} ON ${TabelaVacinas.NOME_TABELA}.${BaseColumns._ID}=$CAMPO_ID_VACINAS"

        var sql = "SELECT $colunas FROM $tabelas"

        if (selection != null) sql += " WHERE $selection"

        if (groupBy != null) {
            sql += " GROUP BY $groupBy"
            if (having != null) " HAVING $having"
        }

        if (orderBy != null) sql += " ORDER BY $NOME_TABELA.$orderBy"

        return db.rawQuery(sql, selectionArgs)
    }

    companion object{
        const val NOME_TABELA = "utentes"
        const val CAMPO_NOME = "nome"
        const val CAMPO_TELEFONE = "telefone"
        const val CAMPO_CONTRIBUINTE = "contribuinte"
        const val CAMPO_DATA_NASCIMENTO = "datanascimento"
        const val CAMPO_DOSE = "dose"
        const val CAMPO_ID_VACINAS = "id_vacina"
        const val CAMPO_EXTERNO_NOME_VACINA = "nomevacina"

        val TODOS_CAMPOS =arrayOf(BaseColumns._ID, CAMPO_NOME, CAMPO_TELEFONE, CAMPO_CONTRIBUINTE,CAMPO_DATA_NASCIMENTO, CAMPO_DOSE,CAMPO_ID_VACINAS, CAMPO_EXTERNO_NOME_VACINA)
    }

}