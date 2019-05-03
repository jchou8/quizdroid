package edu.washington.jchou8.quizdroid

import android.app.Application
import android.util.Log

data class Quiz(val question: String, val options: List<String>, val correct: Int)
data class Topic(val title: String, val shortDesc: String, val longDesc: String, val questions: List<Quiz>)

interface TopicRepository {
    val topics: List<Topic>
}

class LocalTopicRepository : TopicRepository {
    override val topics = listOf(
        Topic(
            "Math",
            "Questions about math",
            "This topic has some mathematical questions.",
            listOf(
                Quiz(
                    "",
                    listOf("2", "3", "4", "5"),
                    3)
            ))
    )
}

class QuizApp: Application() {

    private val topicRepository = LocalTopicRepository()

    override fun onCreate() {
        super.onCreate()
        Log.v("QuizApp", "QuizApp is loaded!")
    }
}