package ru.nortti.filmssearch.view.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.utils.EXTRA_ID
import ru.nortti.filmssearch.view.fragments.*
import ru.nortti.filmssearch.viewModel.viewModels.TransferViewModel
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    var movieId: Int = 0
    var transferViewModel: TransferViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(MainFragment())

        navigationView.inflateMenu(R.menu.bottom_menu)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.films -> {
                    loadFragment(MainFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.favorites -> {
                    loadFragment(FavoriteFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.pending -> {
                    loadFragment(PendingFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.settings -> {
                    loadFragment(SettingsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@MainActivity)

        // Set the alert dialog title
        builder.setTitle(resources.getString(R.string.exit))

        // Display a message on alert dialog
        builder.setMessage(resources.getString(R.string.want_exit))

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
            finish()
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



}


