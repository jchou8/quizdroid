package edu.washington.jchou8.quizdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.topic_overview.*

class TopicOverview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topic_overview)

        val topicPos = intent.getStringExtra("topicPosition").toInt()
        val topicName = resources.getStringArray(R.array.topics)[topicPos]
        val topicID = resources.getStringArray(R.array.topic_ids)[topicPos]
        val topicDesc = resources.getStringArray(R.array.overviews)[topicPos]
        val totalQuestions = resources.getStringArray(resources.getIdentifier(topicID, "array", packageName)).size

        txt_topic.text = topicName
        txt_topicOverview.text = topicDesc
        txt_numquestions.text = "This topic has %d questions.".format(totalQuestions)

        btn_begin.setOnClickListener{
            val intent = Intent(applicationContext, Question::class.java)
            intent.putExtra("topic", topicID)
            intent.putExtra("correct", "0")
            intent.putExtra("total", "0")
            startActivity(intent)
        }
    }
}
