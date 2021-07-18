package com.example.trabalho_final_v2

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import java.util.*

class NovaMarcacaoFragment: Fragment(), LoaderManager.LoaderCallbacks<Cursor>  {

    private lateinit var editTextDose: EditText
    private lateinit var calendarViewAdministracao: CalendarView

    private lateinit var spinnerUtentes: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        DadosApp.fragment= this
        (activity as MainActivity).menuAtual = R.menu.menu_nova_marcacao

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nova_marcacao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextDose = view.findViewById(R.id.editTextDose)
        calendarViewAdministracao = view.findViewById(R.id.calendarViewAdministracao)
        spinnerUtentes =view.findViewById(R.id.spinnerUtentes)

        loaderManager.initLoader(ID_LOADER_MANAGER_UTENTES,null,this)

    }


    fun navegaListaMarcacoes(){
        findNavController().navigate(R.id.action_novaMarcacaoFragment_to_listaMarcacoesFragment)
    }

    fun guardar(){



        val dose = editTextDose.text.toString().toInt()
        if (dose == null) {
            editTextDose.setError("Preencha a dose")
            editTextDose.requestFocus()
            return
        }
        val idUtentes = spinnerUtentes.selectedItemId

        val dataAdministracao = calendarViewAdministracao.date

        val marcacao = Marcacoes(dose = dose,data = Date(dataAdministracao) , idUtentes = idUtentes)



        val uri = activity?.contentResolver?.insert(
            ContentProviderMarcacoes.ENDERECO_MARCACOES,
            marcacao.toContentValues()
        )

        if (uri == null) {
            Snackbar.make(
                editTextDose,
                "erro inserir marcação",
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            "Inserido com sucesso",
            Toast.LENGTH_LONG
        ).show()
        return
        navegaListaMarcacoes()

    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_guardar_novo_marcacao-> guardar()
            R.id.action_cancelar_novo_marcacao-> navegaListaMarcacoes()
            else -> return false
        }
        return true
    }
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderMarcacoes.ENDERECO_UTENTES,
            TabelaUtentes.TODOS_CAMPOS,
            null,null,
            TabelaUtentes.CAMPO_NOME



        )
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is *not* allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See [ FragmentManager.openTransaction()][androidx.fragment.app.FragmentManager.beginTransaction] for further discussion on this.
     *
     *
     * This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     *
     *
     *  *
     *
     *The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a [android.database.Cursor]
     * and you place it in a [android.widget.CursorAdapter], use
     * the [android.widget.CursorAdapter.CursorAdapter] constructor *without* passing
     * in either [android.widget.CursorAdapter.FLAG_AUTO_REQUERY]
     * or [android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER]
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     *  *  The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a [android.database.Cursor] from a [android.content.CursorLoader],
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * [android.widget.CursorAdapter], you should use the
     * [android.widget.CursorAdapter.swapCursor]
     * method so that the old Cursor is not closed.
     *
     *
     *
     * This will always be called from the process's main thread.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        atualizaSpinnerUtentes(data)
    }



    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     *
     * This will always be called from the process's main thread.
     *
     * @param loader The Loader that is being reset.
     */
    override fun onLoaderReset(loader: Loader<Cursor>) {
        atualizaSpinnerUtentes(null)
    }
    private fun atualizaSpinnerUtentes(data: Cursor?) {
        spinnerUtentes.adapter = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data,
            arrayOf(TabelaUtentes.CAMPO_NOME),
            intArrayOf(android.R.id.text1),
            0


        )

    }
    companion object{
        const val ID_LOADER_MANAGER_UTENTES= 0
    }

}