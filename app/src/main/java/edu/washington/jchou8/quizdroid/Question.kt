package edu.washington.jchou8.quizdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.question.*

class Question : AppCompatActivity() {
    private var curSelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question)

        val topicID = intent.getStringExtra("topic")
        var curCorrect = intent.getStringExtra("correct").toInt()
        val curQuestion = intent.getStringExtra("total").toInt()
        val questionText = resources.getStringArray(resources.getIdentifier(topicID, "array", packageName))[curQuestion]
        val options = resources.getStringArray(resources.getIdentifier("%s_answers%d".format(topicID, curQuestion), "array", packageName))
        val shuffled: MutableList<String> = options.toMutableList()
        shuffled.shuffle()

        txt_questionHeader.text = "Question %d".format(curQuestion + 1)
        txt_question.text = questionText


        for (index in 0 until rad_options.childCount) {
            val option = rad_options.getChildAt(index) as RadioButton
            option.text = shuffled[index]
        }

        rad_options.setOnCheckedChangeListener{ _, checkedID ->
            btn_submit.visibility = View.VISIBLE
            curSelected = findViewById<RadioButton>(checkedID).text.toString()
        }

        btn_submit.visibility = View.INVISIBLE
        btn_submit.setOnClickListener {
            val intent = Intent(applicationContext, Answer::class.java)
            intent.putExtra("topic", topicID)
            intent.putExtra("correct", curCorrect.toString())
            intent.putExtra("chosen", curSelected)
            intent.putExtra("total", curQuestion.toString())
            startActivity(intent)
        }
    }
}
