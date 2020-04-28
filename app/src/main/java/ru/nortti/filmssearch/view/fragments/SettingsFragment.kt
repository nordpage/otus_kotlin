package ru.nortti.filmssearch.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.utils.LANGUAGE
import ru.nortti.filmssearch.utils.SharedPreference
import ru.nortti.filmssearch.utils.THEME

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    lateinit var prefs: SharedPreference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = SharedPreference(requireContext())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lang_but.setOnClickListener {
            if (prefs.getValueString(LANGUAGE).equals("ru")) {
                    prefs.save(LANGUAGE, "en")

                } else {
                    prefs.save(LANGUAGE, "ru")
                }
                requireActivity().recreate()
        }
        lang_sub.setText(String.format(getString(R.string.current_lang), getString(R.string.lang)))

        theme_but.setOnClickListener {
            if (prefs.getValueString(THEME).equals("DAY")) {
                prefs.save(THEME, "NIGHT")
            } else {
                prefs.save(THEME, "DAY")
            }
            requireActivity().recreate()
        }
    }

    override fun onResume() {
        super.onResume()
        when(prefs.getValueString(LANGUAGE)) {
            "ru" -> {
                lang_but.setImageResource(R.drawable.ic_en)
            }
            "en" -> {
                lang_but.setImageResource(R.drawable.ic_ru)
            }
        }

        when(prefs.getValueString(THEME)) {
            "DAY" -> {
                theme_but.setImageResource(R.drawable.night)
                theme_sub.text = String.format(getString(R.string.current_theme), getString(R.string.day))
            }
            "NIGHT" -> {
                theme_but.setImageResource(R.drawable.sun)
                theme_sub.text = String.format(getString(R.string.current_theme), getString(R.string.night))
            }
        }
    }

}
