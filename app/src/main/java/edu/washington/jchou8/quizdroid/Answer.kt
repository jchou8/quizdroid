package edu.washington.jchou8.quizdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.answer.*

class Answer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.answer)

        val topicID = intent.getStringExtra("topic")
        var curCorrect = intent.getStringExtra("correct").toInt()
        var curQuestion = intent.getStringExtra("total").toInt()
        val correctOption = resources.getStringArray(resources.getIdentifier("%s_answers%d".format(topicID, curQuestion), "array", packageName))[0]
        val selectedOption = intent.getStringExtra("chosen")
        val totalQuestions = resources.getStringArray(resources.getIdentifier(topicID, "array", packageName)).size

        curQuestion++
        if (selectedOption == correctOption) {
            curCorrect++
        }
        val isLastQuestion = curQuestion == totalQuestions

        txt_correctAnswer.text = correctOption
        txt_yourAnswer.text = selectedOption
        txt_results.text = "You have %s out of %d correct.".format(curCorrect, curQuestion)
        btn_next.text = if (isLastQuestion) "Finish" else "Next"

        btn_next.setOnClickListener{
            val nextActivity = if (isLastQuestion) MainActivity::class.java else Question::class.java
            val intent = Intent(applicationContext, nextActivity)

            if (!isLastQuestion) {
                intent.putExtra("topic", topicID)
                intent.putExtra("correct", curCorrect.toString())
                intent.putExtra("total", curQuestion.toString())
            }
            startActivity(intent)
        }
    }
}
