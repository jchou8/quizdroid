package edu.washington.jchou8.quizdroid

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.takisoft.fix.support.v7.preference.EditTextPreference
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat
import android.content.SharedPreferences


const val PREFS_KEY = "Quizdroid"

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        val prefFrag = QuizPreferenceFragment()
        supportFragmentManager.beginTransaction().run {
            replace(R.id.prefs_container, prefFrag)
            commit()
        }
    }

    class QuizPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }

        override fun onResume() {
            super.onResume()
            val sh = this.preferenceManager.sharedPreferences
            sh.registerOnSharedPreferenceChangeListener(this)
            for (key in sh.all.keys) {
                val pref = this.findPreference(key)
                if (pref is EditTextPreference) {
                    updateSummary(pref)
                }
            }
        }

        override fun onPause() {
            this.preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
            super.onPause()
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            val pref = this.findPreference(key)
            if (this.findPreference(key) is EditTextPreference) {
                updateSummary(pref as EditTextPreference)
            }
        }
        private fun updateSummary(preference: EditTextPreference) {
            preference.summary = preference.text
        }
    }

}