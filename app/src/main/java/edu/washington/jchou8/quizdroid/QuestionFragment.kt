package edu.washington.jchou8.quizdroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

private const val ARG_QUESTION_NUMBER = "questionNumber"
private const val ARG_QUESTION = "question"

class QuestionFragment : Fragment() {
    private var question: Quiz? = null
    private var questionNumber: Int? = null

    private var curSelected: Int? = null
    private var listener: QuestionFragment.OnSubmitListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = it.getParcelable(ARG_QUESTION)
            questionNumber = it.getInt(ARG_QUESTION_NUMBER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.question, container, false)

        rootView.findViewById<TextView>(R.id.txt_questionHeader).text = "Question %d".format(questionNumber)
        rootView.findViewById<TextView>(R.id.txt_question).text = question!!.question

        val radioGroup = rootView.findViewById<RadioGroup>(R.id.rad_options)
        val submitBtn = rootView.findViewById<Button>(R.id.btn_submit)
        for (index in 0 until radioGroup.childCount) {
            val option = radioGroup.getChildAt(index) as RadioButton
            option.text = question!!.options[index]
        }

        radioGroup.setOnCheckedChangeListener{ _, checkedID ->
            submitBtn.visibility = View.VISIBLE
            val checked = radioGroup.findViewById<RadioButton>(checkedID)
            curSelected = radioGroup.indexOfChild(checked)
        }

        submitBtn.visibility = View.INVISIBLE
        submitBtn.setOnClickListener {
            listener!!.onSubmit(curSelected)
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnSubmitListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSubmitListener {
        fun onSubmit(answer: Int?)
    }

    companion object {
        @JvmStatic
        fun newInstance(question: Quiz, questionNumber: Int) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_QUESTION, question)
                    putInt(ARG_QUESTION_NUMBER, questionNumber)
                }
            }
    }
}
