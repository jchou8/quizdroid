package edu.washington.jchou8.quizdroid

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

private const val ARG_CORRECT_ANSWER = "correctAnswer"
private const val ARG_ANSWER = "answer"
private const val ARG_NUM_CORRECT = "numCorrect"
private const val ARG_NUM_TOTAL = "numTotal"
private const val ARG_IS_LAST = "isLast"


class ResultFragment : Fragment() {
    private var correctAnswer: String? = null
    private var answer: String? = null
    private var numCorrect: Int? = null
    private var numTotal: Int? = null
    private var isLast: Boolean = false
    private var listener: OnResultNextListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            correctAnswer = it.getString(ARG_CORRECT_ANSWER)
            answer = it.getString(ARG_ANSWER)
            numCorrect = it.getInt(ARG_NUM_CORRECT)
            numTotal = it.getInt(ARG_NUM_TOTAL)
            isLast = it.getBoolean(ARG_IS_LAST)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.answer, container, false)
        rootView.findViewById<TextView>(R.id.txt_correctAnswer).text = correctAnswer
        rootView.findViewById<TextView>(R.id.txt_yourAnswer).text = answer
        rootView.findViewById<TextView>(R.id.txt_results).text = "You have %s out of %d correct.".format(numCorrect, numTotal)

        val nextBtn = rootView.findViewById<Button>(R.id.btn_next)
        nextBtn.text = if (isLast) "Finish" else "Next"
        nextBtn.setOnClickListener{
            listener!!.onResultNext()
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnResultNextListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnResultNextListener {
        fun onResultNext()
    }

    companion object {
        @JvmStatic
        fun newInstance(correctAnswer: String, answer: String, numCorrect: Int, numTotal: Int, isLast: Boolean) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CORRECT_ANSWER, correctAnswer)
                    putString(ARG_ANSWER, answer)
                    putInt(ARG_NUM_CORRECT, numCorrect)
                    putInt(ARG_NUM_TOTAL, numTotal)
                    putBoolean(ARG_IS_LAST, isLast)
                }
            }
    }
}
