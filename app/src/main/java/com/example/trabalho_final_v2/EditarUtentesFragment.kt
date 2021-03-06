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

class EditarUtentesFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>  {

    private lateinit var editTextNome: EditText
    private lateinit var editTextTelefone: EditText
    private lateinit var editTextContribuinte: EditText
    private lateinit var editTextDose: EditText
    private lateinit var calendarViewDataNascimento: CalendarView

    private lateinit var spinnerVacinas: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        DadosApp.fragment= this
        (activity as MainActivity).menuAtual = R.menu.menu_altera_utente

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_utentes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextNome = view.findViewById(R.id.editTextNome)
        editTextTelefone = view.findViewById(R.id.editTextTelefone)
        editTextContribuinte = view.findViewById(R.id.editTextContruibuinte)
        editTextDose = view.findViewById(R.id.editTextDose)
        calendarViewDataNascimento = view.findViewById(R.id.calendarViewDataNascimento)
        spinnerVacinas =view.findViewById(R.id.spinnerUtentes)

        editTextNome.setText(DadosApp.utentesSelecionado!!.nome)
        val telefone = "Telefone: ${DadosApp.utentesSelecionado!!.dose.toString()}"
        editTextTelefone.setText(telefone)
        val contribuinte = "Contribuinte: ${DadosApp.utentesSelecionado!!.dose.toString()}"
        editTextContribuinte.setText(contribuinte)
        val dose = "Dose: ${DadosApp.utentesSelecionado!!.dose.toString()}"
        editTextDose.setText(dose)
        //calendarViewDataNascimento.setDate(DadosApp.utentesSelecionado!!.dataNascimento.toString().toLong())


        loaderManager.initLoader(ID_LOADER_MANAGER_VACINAS,null,this)

    }


    fun navegaListaUtentes(){
        findNavController().navigate(R.id.action_editarUtentesFragment_to_listaUtentesFragment)
    }

    fun guardar(){

        val nome = editTextNome.text.toString()
        if (nome.isEmpty()) {
            editTextNome.setError("Preencha o nome")
            editTextNome.requestFocus()
            return
        }

        val telefone = editTextTelefone.text.toString()
        if (telefone.isEmpty()) {
            editTextTelefone.setError("preencha o telefone")
            editTextTelefone.requestFocus()
            return
        }

        val contribuinte = editTextContribuinte.text.toString().toInt()
        if (contribuinte == null) {
            editTextContribuinte.setError("preencha o contribuinte")
            editTextContribuinte.requestFocus()
            return
        }

        val dose = editTextDose.text.toString().toInt()
        if (dose == null) {
            editTextDose.setError("Preencha a dose")
            editTextDose.requestFocus()
            return
        }
        val idVacina = spinnerVacinas.selectedItemId



        val dataNascimento = calendarViewDataNascimento.date

        DadosApp.utentesSelecionado!!.nome = nome
        DadosApp.utentesSelecionado!!.dose = dose
        DadosApp.utentesSelecionado!!.dataNascimento = Date(dataNascimento)
        DadosApp.utentesSelecionado!!.contribuinte = contribuinte
        DadosApp.utentesSelecionado!!.telefone = telefone
        DadosApp.utentesSelecionado!!.id_vacina = idVacina



        val uriUtentes = Uri.withAppendedPath(
            ContentProviderMarcacoes.ENDERECO_UTENTES,
            DadosApp.utentesSelecionado!!.id.toString()
        )

        val registos = activity?.contentResolver?.update(
            uriUtentes,
            DadosApp.utentesSelecionado!!.toContentValues(),
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
        navegaListaUtentes()

    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_guardar_edita_Utente-> guardar()
            R.id.action_cancelar_edita_Utente-> navegaListaUtentes()
            else -> return false
        }
        return true
    }
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            ContentProviderMarcacoes.ENDERECO_VACINAS,
            TabelaVacinas.TODOS_CAMPOS,
            null,null,
            TabelaVacinas.CAMPO_NOME



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
        val idVacina = DadosApp.utentesSelecionado!!.id_vacina

        val ultimaVacina = spinnerVacinas.count - 1
        for (i in 0..ultimaVacina){
            if (idVacina == spinnerVacinas.getItemIdAtPosition(i)){
                spinnerVacinas.setSelection(i)
                return
            }
        }
    }
    private fun atualizaSpinnerVacinas(data: Cursor?) {
        spinnerVacinas.adapter = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data,
            arrayOf(TabelaVacinas.CAMPO_NOME),
            intArrayOf(android.R.id.text1),
            0


        )

    }
    companion object{
        const val ID_LOADER_MANAGER_VACINAS= 0
    }

}