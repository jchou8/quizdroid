package edu.washington.jchou8.quizdroid

import android.app.Application
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class QuizActivity : AppCompatActivity(),
    QuestionFragment.OnSubmitListener,
    OverviewFragment.OnOverviewBeginListener,
    ResultFragment.OnResultNextListener {

    private val TAG = "QuizActivity"
    private val QUESTION_FRAGMENT = "QuestionFragment"
    private val OVERVIEW_FRAGMENT = "OverviewFragment"
    private val RESULT_FRAGMENT = "ResultFragment"

    private var topic: String? = null
    private var questions = listOf<String>()
    private var options = mutableListOf<List<String>>()
    private var curQuestion = 0
    private var curCorrect = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        topic = intent.getStringExtra("topic")
        questions = resources.getStringArray(resources.getIdentifier(topic, "array", packageName)).asList()

        for (qid in questions.indices) {
            val qOpts = resources.getStringArray(resources.getIdentifier("%s_answers%d".format(topic, qid), "array", packageName))
            options.add(qid, qOpts.toList())
        }

        val topicPos = intent.getStringExtra("topicPos").toInt()
        val topicName = resources.getStringArray(resources.getIdentifier("topics", "array", packageName))[topicPos]
        val topicDesc = resources.getStringArray(resources.getIdentifier("overviews", "array", packageName))[topicPos]

        val overviewFrag = OverviewFragment.newInstance(topicName, topicDesc, questions.size)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.quiz_container, overviewFrag, OVERVIEW_FRAGMENT)
            commit()
        }
    }

    override fun onSubmit(answer: String?) {
        if (options[curQuestion][0] === answer) {
            curCorrect++
        }

        val resultFrag = ResultFragment.newInstance(
            options[curQuestion][0],
            answer!!,
            curCorrect,
            curQuestion + 1,
            curQuestion + 1 == questions.size)
        supportFragmentManager.beginTransaction().run {
            setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            replace(R.id.quiz_container, resultFrag, RESULT_FRAGMENT)
            addToBackStack(null)
            commit()
        }
    }

    override fun onResultNext() {
        curQuestion++

        val isLast = curQuestion == questions.size
        if (isLast) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        } else {
            nextQuestion()
        }
    }

    override fun onOverviewBegin() {
        nextQuestion()
    }

    private fun nextQuestion() {
        val questionFrag = QuestionFragment.newInstance(
            questions[curQuestion],
            curQuestion + 1,
            options[curQuestion] as ArrayList<String>)

        supportFragmentManager.beginTransaction().run {
            setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            replace(R.id.quiz_container, questionFrag, QUESTION_FRAGMENT)
            addToBackStack(null)
            commit()
        }
    }
}
