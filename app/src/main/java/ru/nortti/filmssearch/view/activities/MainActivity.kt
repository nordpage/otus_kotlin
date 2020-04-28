package ru.nortti.filmssearch.view.activities

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.view.fragments.FavoriteFragment
import ru.nortti.filmssearch.view.fragments.MainFragment
import ru.nortti.filmssearch.view.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val RESULT_CODE = 0
    }

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

                R.id.settings -> {
                    loadFragment(SettingsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }

    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        return when (item.itemId) {
//            R.id.favs -> {
//                startActivity(Intent(this, FavoritesActivity::class.java))
//                return true
//            }
//            R.id.language -> {
//                if (prefs.getValueString(LANGUAGE).equals("RU")) {
//                    prefs.save(LANGUAGE, "EN")
//
//                } else {
//                    prefs.save(LANGUAGE, "RU")
//                }
//                this.recreate()
//                return true
//            }
//            R.id.theme -> {
//                if (prefs.getValueString(THEME).equals("DAY")) {
//                    prefs.save(THEME, "NIGHT")
//                } else {
//                    prefs.save(THEME, "DAY")
//                }
//                this.recreate()
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@MainActivity)

        // Set the alert dialog title
        builder.setTitle(resources.getString(R.string.exit))

        // Display a message on alert dialog
        builder.setMessage(resources.getString(R.string.want_exit))

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
            finish()

        }


        // Display a negative button on alert dialog
        builder.setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
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


