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

    private var topic: Topic? = null
    private var curQuestion = 0
    private var curCorrect = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        topic = intent.getParcelableExtra("topic")

        val topicName = topic!!.title
        val topicDesc = topic!!.longDesc

        val overviewFrag = OverviewFragment.newInstance(topicName, topicDesc, topic!!.questions.size)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.quiz_container, overviewFrag, OVERVIEW_FRAGMENT)
            commit()
        }
    }

    override fun onSubmit(answer: Int?) {
        val question = topic!!.questions[curQuestion]
        if (question.correct - 1 == answer) {
            curCorrect++
        }

        val resultFrag = ResultFragment.newInstance(
            question.options[question.correct - 1],
            question.options[answer!!],
            curCorrect,
            curQuestion + 1,
            curQuestion + 1 == topic!!.questions.size)
        supportFragmentManager.beginTransaction().run {
            setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            replace(R.id.quiz_container, resultFrag, RESULT_FRAGMENT)
            addToBackStack(null)
            commit()
        }
    }

    override fun onResultNext() {
        curQuestion++

        val isLast = curQuestion == topic!!.questions.size
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
            topic!!.questions[curQuestion],
            curQuestion + 1)

        supportFragmentManager.beginTransaction().run {
            setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            replace(R.id.quiz_container, questionFrag, QUESTION_FRAGMENT)
            addToBackStack(null)
            commit()
        }
    }
}
