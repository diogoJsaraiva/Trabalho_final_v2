package com.example.trabalho_final_v2

import android.database.Cursor
import android.net.Uri
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
import java.util.*

class EditarMarcacaoFragment: Fragment(), LoaderManager.LoaderCallbacks<Cursor>  {

    private lateinit var editTextDose: EditText
    private lateinit var calendarViewAdministracao: CalendarView

    private lateinit var spinnerUtentes: Spinner


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        DadosApp.fragment= this
        (activity as MainActivity).menuAtual = R.menu.menu_altera_marcacao

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_marcacoes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextDose = view.findViewById(R.id.editTextDose)
        calendarViewAdministracao = view.findViewById(R.id.calendarViewAdministracao)
        spinnerUtentes =view.findViewById(R.id.spinnerUtentes)


        editTextDose.setText(DadosApp.marcacoesSelecionado!!.dose.toString())

        //calendarViewAdministracao.setDate(DadosApp.marcacoesSelecionado!!.data.toString().toLong())


        loaderManager.initLoader(ID_LOADER_MANAGER_UTENTES,null,this)

    }


    fun navegaListaMarcacao(){
        findNavController().navigate(R.id.action_editarMarcacaoFragment_to_listaMarcacoesFragment)
    }

    fun guardar(){



        val dose = editTextDose.text.toString().toInt()
        if (dose == null) {
            editTextDose.setError("Preencha a dose")
            editTextDose.requestFocus()
            return
        }
        val idutentes = spinnerUtentes.selectedItemId


        val dataAdm = calendarViewAdministracao.date


        DadosApp.marcacoesSelecionado!!.dose = dose
        DadosApp.marcacoesSelecionado!!.data = Date(dataAdm)
        DadosApp.marcacoesSelecionado!!.idUtentes = idutentes



        val uriUtentes = Uri.withAppendedPath(
            ContentProviderMarcacoes.ENDERECO_MARCACOES,
            DadosApp.marcacoesSelecionado!!.id.toString()
        )

        val registos = activity?.contentResolver?.update(
            uriUtentes,
            DadosApp.marcacoesSelecionado!!.toContentValues(),
            null,
            null
        )

        if (registos != 1){
            Toast.makeText(
                requireContext(),
                "Erro ao alterar Utente",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        Toast.makeText(
            requireContext(),
            "Utente alterado com sucesso",
            Toast.LENGTH_LONG
        ).show()
        navegaListaMarcacao()

    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_guardar_edita_marcacao-> guardar()
            R.id.action_cancelar_edita_marcacao-> navegaListaMarcacao()
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
        atualizaSpinnerVacinas(data)
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
        val idVacina = DadosApp.marcacoesSelecionado!!.idUtentes

        val ultimaVacina = spinnerUtentes.count - 1
        for (i in 0..ultimaVacina){
            if (idVacina == spinnerUtentes.getItemIdAtPosition(i)){
                spinnerUtentes.setSelection(i)
                return
            }
        }
    }
    private fun atualizaSpinnerVacinas(data: Cursor?) {
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