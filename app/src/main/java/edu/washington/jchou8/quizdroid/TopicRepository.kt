import android.content.Context
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import edu.washington.jchou8.quizdroid.Quiz
import edu.washington.jchou8.quizdroid.Topic
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

const val TITLE = "title"
const val DESC = "desc"
const val QUESTIONS = "questions"
const val QUESTION_TEXT = "text"
const val QUESTION_ANSWER = "answer"
const val QUESTION_OPTIONS = "answers"
const val DEFAULT_ICON = "ic_launcher"
const val TAG = "TopicRepository"

interface TopicRepository {
    val topics: List<Topic>
}

class LocalFileTopicRepository(stream: InputStream) : TopicRepository {
    override var topics = listOf<Topic>()

    init {
        topics = parseJson(stream)
    }

    private fun parseJson(inputStream: InputStream): List<Topic> {
        val jsonString: String? = try {
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to open file: %s".format(e.message))
            null
        }

        val parsedTopics = mutableListOf<Topic>()
        jsonString?.let {
            val topicsJSONArray = JSONArray(jsonString)
            for (i in 0 until topicsJSONArray.length()) {
                val topicObject = topicsJSONArray.getJSONObject(i)
                val topicName = topicObject.getString(TITLE)
                val topicDesc = topicObject.getString(DESC)
                val questionJSONArray = topicObject.getJSONArray(QUESTIONS)
                val questionList = mutableListOf<Quiz>()

                for (j in 0 until questionJSONArray.length()) {
                    val questionObject = questionJSONArray.getJSONObject(j)
                    val questionText = questionObject.getString(QUESTION_TEXT)
                    val questionAnswer = questionObject.getInt(QUESTION_ANSWER)
                    val questionOptions = questionObject.getJSONArray(QUESTION_OPTIONS)
                    val optionsList = mutableListOf<String>()
                    for (k in 0 until questionOptions.length()) {
                        optionsList.add(questionOptions.getString(k))
                    }

                    questionList.add(Quiz(questionText, optionsList, questionAnswer))
                }

                parsedTopics.add(Topic(topicName, topicDesc, topicDesc, questionList, DEFAULT_ICON))
            }
        }

        return parsedTopics
    }
}

class LocalTopicRepository : TopicRepository {
    override val topics = listOf(
        Topic(
            "Math",
            "Questions about math.",
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
            ),
            "ic_launcher"),
        Topic(
            "Physics",
            "Physical questions.",
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
            ),
            "ic_launcher"),
        Topic(
            "Marvel Superheroes",
            "Marvelous questions.",
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
            ),
            "ic_launcher")
    )
}