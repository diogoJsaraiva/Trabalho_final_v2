package com.example.trabalho_final_v2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    private lateinit var menu: Menu
    var menuAtual = R.menu.menu_main
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
            atualizaMenuListaVacinas(false)
        }
        if (menuAtual == R.menu.menu_lista_utentes) {
            atualizaMenuListaUtentes(false)
        }
        if (menuAtual == R.menu.menu_lista_marcacoes) {
            atualizaMenuListaMarcacoes(false)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val opcaoProcessada = when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "Pessoas v.1.0", Toast.LENGTH_LONG).show()
                true
            }
            else -> when (menuAtual) {
                R.menu.menu_lista_vacinas -> (DadosApp.fragment as ListaVacinasFragment).processaOpcaoMenu(item)
                R.menu.menu_nova_vacina -> (DadosApp.fragment as NovaVacinasFragment).processaOpcaoMenu(item)
                R.menu.menu_eliminar_vacina -> (DadosApp.fragment as EliminaVacinasFragment).processaOpcaoMenu(item)
                R.menu.menu_alterar_vacina -> (DadosApp.fragment as EditarVacinasFragment).processaOpcaoMenu(item)
                R.menu.menu_lista_utentes -> (DadosApp.fragment as ListaUtentesFragment).processaOpcaoMenu(item)
                R.menu.menu_novo_utente -> (DadosApp.fragment as NovoUtentesFragment).processaOpcaoMenu(item)
                R.menu.menu_eliminar_utente -> (DadosApp.fragment as EliminaUtentesFragment).processaOpcaoMenu(item)
                R.menu.menu_altera_utente -> (DadosApp.fragment as EditarUtentesFragment).processaOpcaoMenu(item)
                R.menu.menu_lista_marcacoes -> (DadosApp.fragment as ListaMarcacoesFragment).processaOpcaoMenu(item)
                R.menu.menu_nova_marcacao -> (DadosApp.fragment as NovaMarcacaoFragment).processaOpcaoMenu(item)

                else -> false
            }

        }
        return if (opcaoProcessada) true else super.onOptionsItemSelected(item)
    }


    fun atualizaMenuListaVacinas(permiteAlterarEliminar: Boolean) {
        menu.findItem(R.id.action_eliminar_vacina).setVisible(permiteAlterarEliminar)
        menu.findItem(R.id.action_editar_vacina).setVisible(permiteAlterarEliminar)

    }
    fun atualizaMenuListaUtentes(permiteAlterarEliminar: Boolean) {
        menu.findItem(R.id.action_eliminar_utentes).setVisible(permiteAlterarEliminar)
        menu.findItem(R.id.action_editar_utentes).setVisible(permiteAlterarEliminar)

    }
    fun atualizaMenuListaMarcacoes(permiteAlterarEliminar: Boolean) {
        menu.findItem(R.id.action_eliminar_marcacoes).setVisible(permiteAlterarEliminar)
        menu.findItem(R.id.action_editar_marcacoes).setVisible(permiteAlterarEliminar)

    }
}