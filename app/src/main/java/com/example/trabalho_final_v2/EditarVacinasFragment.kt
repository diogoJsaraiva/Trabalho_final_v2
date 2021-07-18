package com.example.trabalho_final_v2

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager

import androidx.navigation.fragment.findNavController

class EditarVacinasFragment : Fragment(){

    private  lateinit var  editTextNome: EditText





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        DadosApp.fragment = this
        (activity as MainActivity).menuAtual = R.menu.menu_alterar_vacina

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_vacinas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextNome = view.findViewById<EditText>(R.id.editTextVacina)



        editTextNome.setText(DadosApp.vacinasSelecionado!!.nomevacina)


    }


    fun processaOpcaoMenu(item: MenuItem):Boolean{
        when(item.itemId) {
            R.id.action_guardar_edita_Vacina -> guarda()
            R.id.action_cancelar_edita_Vacina-> cancelar()
            else -> return false

        }
        return true
    }


    fun guarda(){

        val nome = editTextNome.text.toString()

        if(nome.isEmpty()){
            editTextNome.setError("Preencha o nome da vacina")
            editTextNome.requestFocus()
            return
        }



        DadosApp.vacinasSelecionado!!.nomevacina = nome


        val urivacinas= Uri.withAppendedPath(
            ContentProviderMarcacoes.ENDERECO_VACINAS,
            DadosApp.vacinasSelecionado!!.id.toString()
        )


        val registos =  activity?.contentResolver?.update(
            urivacinas,
            DadosApp.vacinasSelecionado!!.toContentValues(),
            null,
            null
        )

        if(registos != 1){
            Toast.makeText(requireContext(),"erro alterar vacina", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(requireContext(),"vacina alterada com sucesso", Toast.LENGTH_LONG).show()
        navegaListaLivros()
    }

    private fun navegaListaLivros() {
        findNavController().navigate(R.id.action_editarVacinasFragment_to_listaVacinasFragment)
    }


    fun cancelar(){
        findNavController().navigate(R.id.action_editarVacinasFragment_to_listaVacinasFragment)
    }


}