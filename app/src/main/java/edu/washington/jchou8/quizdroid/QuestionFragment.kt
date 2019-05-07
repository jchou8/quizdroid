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
private const val ARG_OPTIONS = "options"

class QuestionFragment : Fragment() {
    private var question: String? = null
    private var questionNumber: Int? = null
    private var options: List<String>? = null

    private var curSelected: String? = null
    private var listener: QuestionFragment.OnSubmitListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = it.getString(ARG_QUESTION)
            questionNumber = it.getInt(ARG_QUESTION_NUMBER)
            options = it.getStringArrayList(ARG_OPTIONS) as List<String>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.question, container, false)

        val shuffled: MutableList<String> = options!!.toMutableList()
        shuffled.shuffle()

        rootView.findViewById<TextView>(R.id.txt_questionHeader).text = "Question %d".format(questionNumber)
        rootView.findViewById<TextView>(R.id.txt_question).text = question

        val radioGroup = rootView.findViewById<RadioGroup>(R.id.rad_options)
        val submitBtn = rootView.findViewById<Button>(R.id.btn_submit)
        for (index in 0 until radioGroup.childCount) {
            val option = radioGroup.getChildAt(index) as RadioButton
            option.text = shuffled[index]
        }

        radioGroup.setOnCheckedChangeListener{ _, checkedID ->
            submitBtn.visibility = View.VISIBLE
            curSelected = rootView.findViewById<RadioButton>(checkedID).text.toString()
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
        fun onSubmit(answer: String?)
    }

    companion object {
        @JvmStatic
        fun newInstance(question: String, questionNumber: Int, options: ArrayList<String>) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUESTION, question)
                    putInt(ARG_QUESTION_NUMBER, questionNumber)
                    putStringArrayList(ARG_OPTIONS, options)
                }
            }
    }
}
