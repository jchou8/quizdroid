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

private const val ARG_TOPIC = "topic"
private const val ARG_TOPIC_DESC = "topicDesc"
private const val ARG_NUM_QUESTIONS = "numQuestions"

class OverviewFragment : Fragment() {
    private var topic: String? = null
    private var topicDesc: String? = null
    private var numQuestions: Int? = null
    private var listener: OnOverviewBeginListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            topic = it.getString(ARG_TOPIC)
            topicDesc = it.getString(ARG_TOPIC_DESC)
            numQuestions = it.getInt(ARG_NUM_QUESTIONS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.topic_overview, container, false)
        rootView.findViewById<TextView>(R.id.txt_topic).text = topic
        rootView.findViewById<TextView>(R.id.txt_topicOverview).text = topicDesc
        rootView.findViewById<TextView>(R.id.txt_numquestions).text = "This topic has %d questions.".format(numQuestions)
        rootView.findViewById<Button>(R.id.btn_begin).setOnClickListener{
            listener!!.onOverviewBegin()
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnOverviewBeginListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnOverviewBeginListener {
        fun onOverviewBegin()
    }

    companion object {
        @JvmStatic
        fun newInstance(topic: String, topicDesc: String, numQuestions: Int) =
            OverviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TOPIC, topic)
                    putString(ARG_TOPIC_DESC, topicDesc)
                    putInt(ARG_NUM_QUESTIONS, numQuestions)
                }
            }
    }
}
