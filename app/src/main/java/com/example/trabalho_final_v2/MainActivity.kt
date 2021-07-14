package com.example.trabalho_final_v2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    private lateinit var menu: Menu
    var menuAtual = R.menu.menu_lista_vacinas
        set(value) {

            field = value

            invalidateOptionsMenu()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        DadosApp.activity = this
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(menuAtual, menu)
        this.menu = menu
        if (menuAtual == R.menu.menu_lista_vacinas) {
            atualizaMenuListaMARCACOES(false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val opcaoProcessada = when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "PEssoas v.1.0", Toast.LENGTH_LONG).show()
                true
            }
            else -> when (menuAtual) {
                R.menu.menu_lista_vacinas -> (DadosApp.fragment as ListaVacinasFragment).processaOpcaoMenu(
                    item
                )
                else -> false
            }


        }
        return if (opcaoProcessada) true else super.onOptionsItemSelected(item)
    }


    fun atualizaMenuListaMARCACOES(permiteAlterarEliminar: Boolean) {
        menu.findItem(R.id.action_eliminar_vacina).setVisible(permiteAlterarEliminar)
        menu.findItem(R.id.action_editar_vacina).setVisible(permiteAlterarEliminar)

    }
}