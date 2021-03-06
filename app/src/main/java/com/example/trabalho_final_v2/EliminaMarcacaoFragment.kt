package com.example.trabalho_final_v2

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class EliminaMarcacaoFragment: Fragment() {

    private lateinit var textViewNome: TextView

    private lateinit var textViewDose: TextView


    private lateinit var textViewDataAdm: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_eliminar_marcacao

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_elimina_marcacao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewNome = view.findViewById(R.id.textViewNome)

        textViewDose = view.findViewById(R.id.textViewDose)

        textViewDataAdm =view.findViewById(R.id.textViewDataAdministracao)

        textViewNome.setText(DadosApp.marcacoesSelecionado!!.nomeUtente)

        val dose = "Dose: ${DadosApp.marcacoesSelecionado!!.dose.toString()}"
        textViewDose.setText(dose)

        textViewDataAdm.setText(DadosApp.marcacoesSelecionado!!.data.toString())



    }
    private fun navegaListaVacinas() {
        findNavController().navigate(R.id.action_eliminaMarcacaoFragment_to_listaMarcacoesFragment)
    }

    fun processaOpcaoMenu(item: MenuItem):Boolean{
        when(item.itemId) {
            R.id.action_confirma_eliminar_marcacao -> eliminaMarcacao()
            R.id.action_cancelar_eliminar_marcacao -> navegaListaVacinas()
            else -> return false

        }
        return true
    }

    private fun eliminaMarcacao() {
        val urimarcacao = Uri.withAppendedPath(
            ContentProviderMarcacoes.ENDERECO_MARCACOES,
            DadosApp.marcacoesSelecionado!!.id.toString()
        )


        val registos =  activity?.contentResolver?.delete(
            urimarcacao,
            null,
            null
        )

        if(registos != 1){
            Toast.makeText(requireContext(),"erro ao eliminar marca????o", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(requireContext(),"Marca????o eliminada com sucesso", Toast.LENGTH_LONG).show()
        navegaListaVacinas()
    }

}