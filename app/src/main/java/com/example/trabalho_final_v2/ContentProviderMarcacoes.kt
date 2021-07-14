package com.example.trabalho_final_v2



import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class ContentProviderMarcacoes  : ContentProvider(){
    private var bdMarcacoesOpenHelper : BDMarcacoesOpenHelper? = null

    override fun onCreate(): Boolean {
        bdMarcacoesOpenHelper = BDMarcacoesOpenHelper(context)


        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val bd = bdMarcacoesOpenHelper!!.readableDatabase


        return when(getUriMatcher().match(uri)){
            URI_VACINA -> TabelaVacinas(bd).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>,
                null,
                null,
                sortOrder
            )
            URI_VACINA_ESPECIFICO -> TabelaVacinas(bd).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!), // id
                null,
                null,
                null
            )
            URI_UTENTES -> TabelaUtentes(bd).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>,
                null,
                null,
                sortOrder
            )
            URI_UTENTES_ESPECIFICO ->TabelaUtentes(bd).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!), // id
                null,
                null,
                null
            )
            URI_MARCACOES -> TabelaMarcacoes(bd).query(
                projection as Array<String>,
                selection,
                selectionArgs as Array<String>,
                null,
                null,
                sortOrder
            )
            URI_MARCACOES_ESPECIFICO->TabelaMarcacoes(bd).query(
                projection as Array<String>,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!), // id
                null,
                null,
                null
            )
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return when(getUriMatcher().match(uri)){
            URI_VACINA -> "$MULTIPLOS_ITEMS/$VACINA"
            URI_VACINA_ESPECIFICO -> "$UNICO_ITEM/$VACINA"
            URI_UTENTES-> "$MULTIPLOS_ITEMS/$UTENTES"
            URI_UTENTES_ESPECIFICO -> "$UNICO_ITEM/$UTENTES"
            URI_MARCACOES -> "$MULTIPLOS_ITEMS/$MARCACOES"
            URI_MARCACOES_ESPECIFICO -> "$UNICO_ITEM/$MARCACOES"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val bd = bdMarcacoesOpenHelper!!.writableDatabase

        val id = when(getUriMatcher().match(uri)){
            URI_VACINA -> TabelaVacinas(bd).insert(values!!)
            URI_UTENTES -> TabelaUtentes(bd).insert(values!!)
            URI_MARCACOES-> TabelaMarcacoes(bd).insert(values!!)
            else -> -1
        }

        if (id == -1L) return null
        return Uri.withAppendedPath(uri, id.toString())
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val bd = bdMarcacoesOpenHelper!!.writableDatabase

        return when(getUriMatcher().match(uri)){
            URI_VACINA_ESPECIFICO -> TabelaVacinas(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!) // id
            )
            URI_UTENTES_ESPECIFICO -> TabelaUtentes(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!) // id
            )
            URI_MARCACOES_ESPECIFICO ->TabelaMarcacoes(bd).delete(
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!) // id
            )
            else -> 0
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val bd = bdMarcacoesOpenHelper!!.writableDatabase

        return when(getUriMatcher().match(uri)){
            URI_VACINA_ESPECIFICO -> TabelaVacinas(bd).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!) // id
            )
            URI_UTENTES_ESPECIFICO ->TabelaUtentes(bd).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!) // id
            )
            URI_MARCACOES_ESPECIFICO ->TabelaMarcacoes(bd).update(
                values!!,
                "${BaseColumns._ID}=?",
                arrayOf(uri.lastPathSegment!!) // id
            )
            else -> 0
        }
    }

    companion object{
        private const val AUTHORITY = "com.example.projetofinalandroid"
        private const val VACINA = "vacina"
        private const val UTENTES = "utentes"
        private const val MARCACOES = "marcacoes"

        private const val URI_VACINA = 100
        private const val URI_VACINA_ESPECIFICO = 101
        private const val URI_UTENTES = 200
        private const val URI_UTENTES_ESPECIFICO = 201
        private const val URI_MARCACOES = 300
        private const val URI_MARCACOES_ESPECIFICO = 301

        private const val MULTIPLOS_ITEMS = "vnd,android.cursor.dir"
        private const val UNICO_ITEM = "vnd,android.cursor.item"

        private val ENDERECO_BASE = Uri.parse("content://$AUTHORITY")
        public val ENDERECO_VACINAS = Uri.withAppendedPath(ENDERECO_BASE, VACINA)
        public val ENDERECO_Pessoas = Uri.withAppendedPath(ENDERECO_BASE, UTENTES)
        public val ENDERECO_MARCACOES = Uri.withAppendedPath(ENDERECO_BASE, MARCACOES)

        private fun getUriMatcher() : UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            uriMatcher.addURI(AUTHORITY, VACINA, URI_VACINA)
            uriMatcher.addURI(AUTHORITY, "$VACINA/#", URI_VACINA_ESPECIFICO)
            uriMatcher.addURI(AUTHORITY, UTENTES, URI_UTENTES)
            uriMatcher.addURI(AUTHORITY, "$UTENTES/#", URI_UTENTES_ESPECIFICO)
            uriMatcher.addURI(AUTHORITY, MARCACOES, URI_MARCACOES)
            uriMatcher.addURI(AUTHORITY, "$MARCACOES/#", URI_MARCACOES_ESPECIFICO)

            return uriMatcher
        }
    }

}