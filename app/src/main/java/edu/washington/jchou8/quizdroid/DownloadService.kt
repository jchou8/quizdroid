package edu.washington.jchou8.quizdroid

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import java.io.File
import java.io.IOException

class DownloadService : IntentService("DownloadService") {
    private val TAG = "DownloadService"
    private val filename = "questions.json"
    private val handler = Handler()
    private var intent: Intent? = null
    private var pendingIntent: PendingIntent? = null
    private var builder: NotificationCompat.Builder? = null

    override fun onCreate() {
        super.onCreate()
        intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        builder = NotificationCompat.Builder(applicationContext, "Quizdroid")
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle("Quizdroid")
            .setContentText("Downloading questions...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    override fun onHandleIntent(intent: Intent) {
        val url = intent.getStringExtra("url")
        Log.i(TAG, "Downloading from URL " + url)
        handler.post{
            Toast.makeText(applicationContext, "Downloading from URL " + url, Toast.LENGTH_SHORT).show()
        }

        updateNotification("Downloading questions...", R.drawable.ic_download)

        val queue = Volley.newRequestQueue(applicationContext)
        val jsonRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                try {
                    openFileOutput(filename, Context.MODE_PRIVATE).use {
                        it.write(response.toString().toByteArray())
                    }
                    QuizApp.updateTopics(applicationContext)
                    updateNotification("Successfully downloaded questions.", R.drawable.ic_done)
                } catch(e: IOException) {
                    updateNotification("Download failed!", R.drawable.ic_error)
                    Log.e(TAG, "Failed to save questions.json: %s".format(e.toString()))
                }
            },
            Response.ErrorListener {error ->
                updateNotification("Download failed!", R.drawable.ic_error)
                Log.e(TAG, "Request failed: %s".format(error.toString()))
            }
        )

        queue.add(jsonRequest)
    }

    private fun updateNotification(message: String, icon: Int) {
        builder!!.setContentText(message)
            .setSmallIcon(icon)
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(0, builder!!.build())
        }
    }
}
