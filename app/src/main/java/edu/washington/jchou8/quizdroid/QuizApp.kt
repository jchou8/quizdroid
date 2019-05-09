package edu.washington.jchou8.quizdroid

import android.app.Application
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
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp", "QuizApp is loaded!")

        try {
            val inputStream = assets.open(FILE_PATH)
            topicRepository = LocalFileTopicRepository(inputStream)
        } catch (e: IOException){
            Log.e(TAG, "Failed to load topic repository: %s".format(e.message))
        }
    }
}