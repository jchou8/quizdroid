package edu.washington.jchou8.quizdroid

import android.app.Application
import android.content.Context
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
}
