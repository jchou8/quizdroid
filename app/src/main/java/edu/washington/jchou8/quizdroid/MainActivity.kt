package edu.washington.jchou8.quizdroid

import android.app.Application
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val topics = resources.getStringArray(R.array.topics).asList()
        val topicIDs = resources.getStringArray(R.array.topic_ids).asList()
        val adapter = TopicsRecyclerViewAdapter(topics)
        adapter.onTopicClickedListener = { position, _ ->
            val intent = Intent(applicationContext, QuizActivity::class.java)
            intent.putExtra("topicPos", position.toString())
            intent.putExtra("topic", topicIDs[position])
            startActivity(intent)
        }

        view_topics.adapter = adapter
        view_topics.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

    }
}
