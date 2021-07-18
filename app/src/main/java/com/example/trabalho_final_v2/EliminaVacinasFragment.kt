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

class EliminaVacinasFragment: Fragment() {
    private  lateinit var  textViewNome: TextView



override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

    DadosApp.fragment = this
    (activity as MainActivity).menuAtual = R.menu.menu_eliminar_vacina

    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_elimina_vacina, container, false)
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    textViewNome = view.findViewById(R.id.textViewNomeElimina)


    textViewNome.setText(DadosApp.vacinasSelecionado!!.nomevacina)


}
private fun navegaListaVacinas() {
    findNavController().navigate(R.id.action_eliminaVacinasFragment_to_listaVacinasFragment)
}

fun processaOpcaoMenu(item: MenuItem):Boolean{
    when(item.itemId) {
        R.id.action_confirma_eliminar_vacina -> eliminaVacina()
        R.id.action_cancelar_eliminar_vacina -> navegaListaVacinas()
        else -> return false

    }
    return true
}

private fun eliminaVacina() {
    val urilivro = Uri.withAppendedPath(
        ContentProviderMarcacoes.ENDERECO_VACINAS,
        DadosApp.vacinasSelecionado!!.id.toString()
    )


    val registos =  activity?.contentResolver?.delete(
        urilivro,
        null,
        null
    )

    if(registos != 1){
        Toast.makeText(requireContext(),"erro ao eliminar vacina", Toast.LENGTH_LONG).show()
        return
    }
    Toast.makeText(requireContext(),"Vacina eliminada com sucesso", Toast.LENGTH_LONG).show()
    navegaListaVacinas()
}

}