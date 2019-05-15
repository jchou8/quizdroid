package edu.washington.jchou8.quizdroid

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuInflater
import android.os.Build
import android.support.v4.app.DialogFragment


class MainActivity : AppCompatActivity(), NoConnectionDialog.NoConnectionDialogListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkConnection()
        buildTopicList()
    }

    override fun onResume() {
        super.onResume()
        checkConnection()
        buildTopicList()
    }

    private fun checkConnection() {
        if (isAirplaneModeOn(applicationContext)) {
            val airplaneFrag = AirplaneModeDialog()
            airplaneFrag.show(supportFragmentManager, "airplanemode")
        } else if (!hasConnection()) {
            val connectionDialog = NoConnectionDialog()
            connectionDialog.show(supportFragmentManager, "noconnection")
        } else {
            QuizApp.startDownloadService(applicationContext)
        }
    }

    private fun buildTopicList() {
        val topics = QuizApp.getTopics()
        val topicIcons = topics.map{t -> resources.getIdentifier(t.icon, "drawable", packageName)}
        val adapter = TopicsRecyclerViewAdapter(topics, topicIcons)
        adapter.onTopicClickedListener = { position, _ ->
            val intent = Intent(applicationContext, QuizActivity::class.java)
            intent.putExtra("topic", topics[position])
            startActivity(intent)
        }

        view_topics.adapter = adapter
        view_topics.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.preferences, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_preferences -> {
            val intent = Intent(applicationContext, PreferencesActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // Taken from https://stackoverflow.com/questions/4319212/how-can-one-detect-airplane-mode-on-android
    private fun isAirplaneModeOn(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.getInt(
                context.contentResolver,
                Settings.System.AIRPLANE_MODE_ON, 0
            ) != 0
        } else {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0
        }
    }

    private fun hasConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(applicationContext, "Device is offline; cannot download questions.", Toast.LENGTH_LONG).show()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        finish()
        startActivity(intent)
    }
}
