package edu.washington.jchou8.quizdroid

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.json.JSONObject
import java.io.File
import java.io.IOException
import TopicRepository
import LocalFileTopicRepository
import android.app.*
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.SystemClock
import android.renderscript.ScriptGroup
import java.io.InputStream

const val TAG = "QuizApp"
const val FILE_PATH = "questions.json"

@Parcelize
data class Quiz(val question: String, val options: List<String>, val correct: Int): Parcelable

@Parcelize
data class Topic(val title: String, val shortDesc: String, val longDesc: String, val questions: List<Quiz>, val icon: String):
    Parcelable

class QuizApp: Application() {

    companion object {
        private var topicRepository: TopicRepository? = null
        fun getTopics() = topicRepository!!.topics
        fun updateTopics(ctx: Context) {
            try {
                val inputStream = ctx.openFileInput(FILE_PATH)
                topicRepository = LocalFileTopicRepository(inputStream)
            } catch (e: IOException){
                Log.e(TAG, "Failed to load topic repository: %s".format(e.message))
            }
        }

        private var pendingIntent: PendingIntent? = null

        fun startDownloadService(ctx: Context) {
            val prefs = ctx.getSharedPreferences(ctx.packageName + "_preferences", Context.MODE_PRIVATE)
            val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(ctx, DownloadService::class.java)
            val url = prefs.getString("download_url", "http://tednewardsandbox.site44.com/questions.json")
            val freq = prefs.getString("download_freq", "1").toLong() * 60 * 1000

            Log.i(TAG, "Starting download service from %s every %d ms".format(url, freq))

            intent.putExtra("url", url)
            pendingIntent = PendingIntent.getService(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(),
                freq,
                pendingIntent)

        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp", "QuizApp is loaded!")
        createNotificationChannel()
        updateTopics(applicationContext)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Quizdroid"
            val descriptionText = "A quiz app."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(name, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}