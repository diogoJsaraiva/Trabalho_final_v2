package com.example.trabalho_final_v2

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class EliminaUtentesFragment: Fragment() {

    private lateinit var textViewNome: TextView
    private lateinit var textViewTelefone: TextView
    private lateinit var textViewContribuinte: TextView
    private lateinit var textViewDose: TextView
    private lateinit var textViewDataNascimento: TextView

    private lateinit var textViewDataVacina: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_eliminar_utente

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_elimina_utente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewNome = view.findViewById(R.id.textViewNome)
        textViewTelefone = view.findViewById(R.id.textViewTelefone)
        textViewContribuinte = view.findViewById(R.id.textViewContribuinte)
        textViewDose = view.findViewById(R.id.textViewDose)
        textViewDataNascimento = view.findViewById(R.id.textViewIdade)
        textViewDataVacina =view.findViewById(R.id.TextViewVacina)

        textViewNome.setText(DadosApp.utentesSelecionado!!.nome)
        val telefone = "Telefone: ${DadosApp.utentesSelecionado!!.dose.toString()}"
        textViewTelefone.setText(telefone)
        val contribuinte = "Contribuinte: ${DadosApp.utentesSelecionado!!.dose.toString()}"
        textViewContribuinte.setText(contribuinte)
        val dose = "Dose: ${DadosApp.utentesSelecionado!!.dose.toString()}"
        textViewDose.setText(dose)
        textViewDataNascimento.setText(DadosApp.utentesSelecionado!!.dataNascimento.toString())
        textViewDataVacina.setText(DadosApp.utentesSelecionado!!.nomeVacina)



    }
    private fun navegaListaVacinas() {
        findNavController().navigate(R.id.action_eliminaUtentesFragment_to_listaUtentesFragment)
    }

    fun processaOpcaoMenu(item: MenuItem):Boolean{
        when(item.itemId) {
            R.id.action_confirma_eliminar_utente -> eliminaUtentes()
            R.id.action_cancelar_eliminar_utente -> navegaListaVacinas()
            else -> return false

        }
        return true
    }

    private fun eliminaUtentes() {
        val urilivro = Uri.withAppendedPath(
            ContentProviderMarcacoes.ENDERECO_UTENTES,
            DadosApp.utentesSelecionado!!.id.toString()
        )


        val registos =  activity?.contentResolver?.delete(
            urilivro,
            null,
            null
        )

        if(registos != 1){
            Toast.makeText(requireContext(),"erro ao eliminar utente", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(requireContext(),"utente eliminada com sucesso", Toast.LENGTH_LONG).show()
        navegaListaVacinas()
    }

}