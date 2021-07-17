package com.example.trabalho_final_v2

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TesteBaseDados {
    private fun getAppContext() = InstrumentationRegistry.getInstrumentation().targetContext
    private fun getBDMarcacoesOpenHelper() = BDMarcacoesOpenHelper(getAppContext())

    private fun getTabelaUtentes(db: SQLiteDatabase) = TabelaUtentes(db)
    private fun getTabelaVacinas(db: SQLiteDatabase) = TabelaVacinas(db)
    private fun getTabelaMarcacoes(db: SQLiteDatabase) = TabelaMarcacoes(db)

    private fun insereUtentes(tabelaUtentes: TabelaUtentes, utente: Utentes): Long {
        val id = tabelaUtentes.insert(utente.toContentValues())
        assertNotEquals(-1, id)
        return id
    }

    private fun insereVacinas(tabelaVacinas: TabelaVacinas, vacina: Vacinas): Long {
        val id = tabelaVacinas.insert(vacina.toContentValues())
        assertNotEquals(-1, id)
        return id
    }

    private fun insereMarcacoes(tabelaMarcacoes: TabelaMarcacoes, marcacoes: Marcacoes): Long {
        val id = tabelaMarcacoes.insert(marcacoes.toContentValues())
        assertNotEquals(-1, id)
        return id
    }

    private fun getUtentesBD(
        tabelaUtentes: TabelaUtentes, id: Long ): Utentes {
        val cursor = tabelaUtentes.query(
            TabelaUtentes.TODOS_CAMPOS,
            "${TabelaUtentes.NOME_TABELA}.${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        assertNotNull(cursor)
        assert(cursor!!.moveToNext())
        return Utentes.fromCursor(cursor)
    }

    private fun getVacinaBD(
        tabelaVacinas: TabelaVacinas, id: Long    ): Vacinas {
        val cursor = tabelaVacinas.query(
            TabelaVacinas.TODOS_CAMPOS,
            "${TabelaVacinas.NOME_TABELA}.${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        assertNotNull(cursor)
        assert(cursor!!.moveToNext())
        return Vacinas.fromCursor(cursor)
    }

    private fun getMarcacaoBD(
        tabelaMarcacoes: TabelaMarcacoes, id: Long  ): Marcacoes {
        val cursor = tabelaMarcacoes.query(
            TabelaMarcacoes.TODOS_CAMPOS,
            "${TabelaMarcacoes.NOME_TABELA}.${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        assertNotNull(cursor)
        assert(cursor!!.moveToNext())
        return Marcacoes.fromCursor(cursor)
    }

    @Before
    fun apagaBaseDados() {
        getAppContext().deleteDatabase(BDMarcacoesOpenHelper.NOME_BASE_DADOS)
    }

    @Test
    fun consegueAbrirBaseDados(){

        val db = getBDMarcacoesOpenHelper().readableDatabase
        assert(db.isOpen)

        db.close()
    }

    @Test
    fun consegueInserirVacinas(){
        val db = getBDMarcacoesOpenHelper().writableDatabase
        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas(nomeVacina = "AstraZeneca")

        vacina.id = insereVacinas(tabelaVacinas, vacina)

        val vacinaBD = getVacinaBD(tabelaVacinas, vacina.id)
        assertEquals(vacina, vacinaBD)
        db.close()
    }

    @Test
    fun consegueAlterarVacinas(){
        val db = getBDMarcacoesOpenHelper().writableDatabase
        val tabelaVacinas = getTabelaVacinas(db)
        val vacina = Vacinas(nomeVacina ="?")

        vacina.id = insereVacinas(tabelaVacinas, vacina)


        vacina.nomeVacina = "Pfizer"


        val registosAlterados = tabelaVacinas.update(
            vacina.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(vacina.id.toString())
        )

        assertEquals(1, registosAlterados)
        val livroBD = getVacinaBD(tabelaVacinas, vacina.id)
        assertEquals(vacina, livroBD)

        db.close()
    }

    @Test
    fun consegueApagarVacinas(){
        val db = getBDMarcacoesOpenHelper().writableDatabase
        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas(nomeVacina ="?")

        vacina.id = insereVacinas(tabelaVacinas, vacina)

        val registosEliminados = tabelaVacinas.delete(
            "${BaseColumns._ID}=?",
            arrayOf(vacina.id.toString())
        )

        assertEquals(1, registosEliminados)

        db.close()
    }

    @Test
    fun consegueLerVacinas(){
        val db = getBDMarcacoesOpenHelper().writableDatabase
        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas(nomeVacina ="Moderna" )

        vacina.id = insereVacinas(tabelaVacinas, vacina)

        val vacinaBD = getVacinaBD(tabelaVacinas, vacina.id)
        assertEquals(vacina, vacinaBD)

        db.close()
    }

    @Test
    fun consegueInserirPessoas(){
        val db = getBDMarcacoesOpenHelper().writableDatabase

        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas(nomeVacina ="astrazena")

        vacina.id = insereVacinas(tabelaVacinas, vacina)

        val tabelaUtentes = getTabelaUtentes(db)
        val utentes = Utentes(nome="João Pascoa", telefone = "+355 932654368",dose = 0,dataNascimento = Date(1980-1900,10,13), contribuinte = 1231324982,vacina = vacina.id )

        utentes.id = insereUtentes(tabelaUtentes, utentes)
        val utentesBD = getUtentesBD(tabelaUtentes, utentes.id)
        assertEquals(utentes, utentesBD)
        db.close()
    }

    @Test
    fun consegueAlterarPessoas(){
        val db = getBDMarcacoesOpenHelper().writableDatabase


        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas(nomeVacina ="pfizer")

        vacina.id = insereVacinas(tabelaVacinas, vacina)



        val tabelaUtentes = getTabelaUtentes(db)
        val utentes = Utentes(nome="?", telefone = "?",dose = 0, dataNascimento = Date(),contribuinte =0 ,vacina = vacina.id)

        utentes.id = insereUtentes(tabelaUtentes, utentes)
        utentes.nome="Arminda"
        utentes.telefone="+355 969773894"
        utentes.contribuinte=123412445
        utentes.dataNascimento=Date(1997-1900, 5,25)
        utentes.dose=1
        utentes.vacina = vacina.id
        val registosAlterados = tabelaUtentes.update(
            utentes.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(utentes.id.toString())
        )

        assertEquals(1, registosAlterados)

        db.close()
    }

    @Test
    fun consegueApagarPessoas(){
        val db = getBDMarcacoesOpenHelper().writableDatabase

        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas(nomeVacina ="moderna")

        vacina.id = insereVacinas(tabelaVacinas, vacina)


        val tabelaUtentes = getTabelaUtentes(db)
        val utentes = Utentes(nome="?", telefone = "?",contribuinte = 0, dataNascimento = Date(), dose = 0,vacina = vacina.id)

        utentes.id = insereUtentes(tabelaUtentes, utentes)

        val registosEliminados =tabelaUtentes.delete(
            "${BaseColumns._ID}=?",
            arrayOf(utentes.id.toString())
        )

        assertEquals(1, registosEliminados)

        db.close()
    }

    @Test
    fun consegueLerPessoas(){
        val db = getBDMarcacoesOpenHelper().writableDatabase

        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas(nomeVacina = "Pfizer")

        vacina.id = insereVacinas(tabelaVacinas, vacina)



        val tabelaUtentes = getTabelaUtentes(db)
        val utentes = Utentes(nome="Arnaldo", telefone = "+355 914347958",contribuinte = 12341254, dataNascimento = Date(1953-1900,1,31), dose = 2,vacina = vacina.id)

        utentes.id = insereUtentes(tabelaUtentes, utentes)

        val utenteBD = getUtentesBD(tabelaUtentes, utentes.id)
        assertEquals(utentes, utenteBD)

        db.close()
    }

    @Test
    fun consegueInserirMarcacao(){
        val db = getBDMarcacoesOpenHelper().writableDatabase



        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas(nomeVacina = "AstrZeneca")

        vacina.id = insereVacinas(tabelaVacinas, vacina)

        val tabelaUtentes= getTabelaUtentes(db)
        val utentes = Utentes(nome="Jorge", telefone = "+355 962346800",contribuinte = 325858758, dataNascimento = Date(1987-1900,2,1), dose = 2 ,vacina = vacina.id)

        utentes.id = insereUtentes(tabelaUtentes, utentes)




        val tabelaDoses = getTabelaMarcacoes(db)
        val marcacoes = Marcacoes(data = Date(1978-1900,10,13), idUtentes = utentes.id,dose = 2)

        marcacoes.id = insereMarcacoes(tabelaDoses, marcacoes)


        val dosesBD = getMarcacaoBD(tabelaDoses, marcacoes.id)
        assertEquals(marcacoes, dosesBD)
        db.close()
    }

    @Test
    fun consegueAlterarDoses(){
        val db = getBDMarcacoesOpenHelper().writableDatabase

        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas( nomeVacina = "AsZeneca")

        vacina.id = insereVacinas(tabelaVacinas, vacina)

        val tabelaUtente = getTabelaUtentes(db)
        val utente = Utentes(nome="Diogo", telefone = "+355 962345577",contribuinte = 459812398, dataNascimento = Date(1932-1900), dose = 2,vacina = vacina.id)

        utente.id = insereUtentes(tabelaUtente, utente)




        val tabelaMarcacoes = getTabelaMarcacoes(db)
        val marcacoes = Marcacoes(data = Date(1999+1900,2,13),idUtentes = utente.id, dose = 2 )

        marcacoes.id = insereMarcacoes(tabelaMarcacoes, marcacoes)

        val registosAlterados = tabelaMarcacoes.update(
            marcacoes.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(marcacoes.id.toString())
        )

        assertEquals(1, registosAlterados)

        db.close()
    }

    @Test
    fun consegueApagarMarcacao(){
        val db = getBDMarcacoesOpenHelper().writableDatabase

        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas(nomeVacina = "AstrZeneca")

        vacina.id = insereVacinas(tabelaVacinas, vacina)

        val tabelaUtentes = getTabelaUtentes(db)
        val utentes = Utentes(nome="Ana", telefone = "+355 962967862",contribuinte = 48374390, dataNascimento = Date(1983,5,13), dose = 3, vacina = vacina.id)

        utentes.id = insereUtentes(tabelaUtentes, utentes)




        val tabelaMarcacao= getTabelaMarcacoes(db)
        val marcacoes = Marcacoes(data = Date(), idUtentes = utentes.id,dose = 1)
        marcacoes.data = Date(1999-1900,10,12)

        marcacoes.id = insereMarcacoes(tabelaMarcacao, marcacoes)





        val registosEliminados =tabelaMarcacao.delete(
            "${BaseColumns._ID}=?",
            arrayOf(marcacoes.id.toString())
        )

        assertEquals(1, registosEliminados)

        db.close()
    }

    @Test
    fun consegueLerMarcacao(){
        val db = getBDMarcacoesOpenHelper().writableDatabase

        val tabelaVacinas = getTabelaVacinas(db);
        val vacina = Vacinas(nomeVacina = "Pfizer")

        vacina.id = insereVacinas(tabelaVacinas, vacina)


        val tabelaUtente = getTabelaUtentes(db)
        val utente = Utentes(nome="Telmo", telefone = "+355 962973210",contribuinte = 5483478, dataNascimento = Date(1999-1900,12,12), dose = 1, vacina = vacina.id)

        utente.id = insereUtentes(tabelaUtente, utente)



        val tabelaMarcacao= getTabelaMarcacoes(db)
        val marcacoes = Marcacoes(data = Date(2000-1900,10,10), idUtentes = utente.id , dose = 1)

        marcacoes.id = insereMarcacoes(tabelaMarcacao, marcacoes)


        val dosesBD = getMarcacaoBD(tabelaMarcacao, marcacoes.id)
        assertEquals(marcacoes, dosesBD)
        db.close()
    }

}