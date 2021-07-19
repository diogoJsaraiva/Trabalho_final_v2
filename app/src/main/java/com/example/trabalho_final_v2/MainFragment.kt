package com.example.trabalho_final_v2

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).menuAtual = R.menu.menu_main
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_Utentes).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_listaUtentesFragment)

        }
        view.findViewById<Button>(R.id.button_Marcacao).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_listaMarcacoesFragment)

        }
        view.findViewById<Button>(R.id.button_Vacinas).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_listaVacinasFragment)

        }

    }



}