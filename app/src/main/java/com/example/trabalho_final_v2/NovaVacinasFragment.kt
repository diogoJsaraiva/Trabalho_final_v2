package com.example.trabalho_final_v2



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar


class NovaVacinasFragment : Fragment() {

    private lateinit var editTextVacina: EditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        DadosApp.fragment= this
        (activity as MainActivity).menuAtual = R.menu.menu_nova_vacina

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_novas_vacinas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextVacina = view.findViewById(R.id.editTextVacina)

    }

    fun navegaListaVacinas(){
       findNavController().navigate(R.id.action_novaVacinasFragment_to_listaVacinasFragment)
    }

    fun guardar(){

        val vacina = editTextVacina.text.toString()
        if (vacina.isEmpty()){
            editTextVacina.setError("Erro")
            editTextVacina.requestFocus()
            return
        }
        val vacinas = Vacinas(nomeVacina= vacina)

        val uri = activity?.contentResolver?.insert(
            ContentProviderMarcacoes.ENDERECO_VACINAS,
            vacinas.toContentValues()
        )
        if(uri == null) {
            Snackbar.make(
                editTextVacina,
                "erro ao inserir",
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


    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_guardar_novo_vacina -> guardar()
            R.id.action_cancelar_novo_vacina-> navegaListaVacinas()
            else -> return false
        }
        return true
    }

}