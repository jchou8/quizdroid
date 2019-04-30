package edu.washington.jchou8.quizdroid

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.topic.view.*

class TopicsRecyclerViewAdapter(private val topicList: List<String>): RecyclerView.Adapter<TopicsRecyclerViewAdapter.TopicViewHolder>() {
    var onTopicClickedListener: ((position: Int, name: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewHolderType: Int): TopicViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context).inflate(R.layout.topic, parent, false)
        return TopicViewHolder(itemLayoutView)
    }

    override fun getItemCount(): Int {
        return topicList.size
    }

    override fun onBindViewHolder(viewHolder: TopicViewHolder, position: Int) {
        val topic = topicList[position]
        viewHolder.bindTopic(topic, position)
    }

    inner class TopicViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindTopic(name: String, position: Int) {
            itemView.btn_topic.text = name
            itemView.btn_topic.setOnClickListener{
                onTopicClickedListener?.invoke(position, name)
            }
        }
    }
}