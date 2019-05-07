package edu.washington.jchou8.quizdroid

import android.app.Application
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Quiz(val question: String, val options: List<String>, val correct: Int): Parcelable

@Parcelize
data class Topic(val title: String, val shortDesc: String, val longDesc: String, val questions: List<Quiz>): Parcelable

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
                    "What is 3+3?",
                    listOf("3", "4", "5", "6"),
                    3),
                Quiz(
                    "What is 5-2?",
                    listOf("2", "3", "4", "5"),
                    1),
                Quiz(
                    "What is 2^10?",
                    listOf("1024", "2048", "4096", "8192"),
                    0)
            )),
        Topic(
            "Physics",
            "Physical questions",
            "This topic has some questions about physics.",
            listOf(
                Quiz(
                    "What is the speed of light?",
                    listOf("3x10^7 m/s", "3x10^8 m/s", "3x10^9 m/s", "3x10^8 m/hr"),
                    1),
                Quiz(
                    "What is acceleration of gravity on Earth?",
                    listOf("8.9 m/s", "8.9 m/s²", "9.8 m/s", "9.8 m/s²"),
                    3),
                Quiz(
                    "What is the formula for kinetic energy?",
                    listOf("½mv", "¼mv", "½mv²", "¼mv²"),
                    2)
            )),
        Topic(
            "Marvel Superheroes",
            "Marvelous questions",
            "This topic has some questions about Marvel superheroes.",
            listOf(
                Quiz(
                    "Which of the following is not a Marvel superhero?",
                    listOf("Batman", "Iron Man", "Spiderman", "Wolverine"),
                    0),
                Quiz(
                    "How many superheroes are in the Fantastic Four?",
                    listOf("1", "4", "5", "80"),
                    1)
            ))
    )
}

class QuizApp: Application() {
    companion object {
        private val topicRepository = LocalTopicRepository()
        fun getTopics() = topicRepository.topics
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp", "QuizApp is loaded!")
    }
}