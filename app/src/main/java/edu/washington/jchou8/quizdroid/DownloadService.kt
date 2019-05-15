package edu.washington.jchou8.quizdroid

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.os.Handler
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

    override fun onHandleIntent(intent: Intent) {
        val url = intent.getStringExtra("url")
        Log.i(TAG, "Downloading from URL " + url)
        handler.post{
            Toast.makeText(applicationContext, "Downloading from URL " + url, Toast.LENGTH_SHORT).show()
        }

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
                } catch(e: IOException) {
                    Log.e(TAG, "Failed to save questions.json: %s".format(e.toString()))
                }
            },
            Response.ErrorListener {error ->
                Log.e(TAG, "Request failed: %s".format(error.toString()))
            }
        )

        queue.add(jsonRequest)
    }
}
