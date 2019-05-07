package edu.washington.jchou8.quizdroid

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.topic.view.*
import kotlinx.android.synthetic.main.topic_overview.view.*

class TopicsRecyclerViewAdapter(private val topicList: List<Topic>, private val topicIcons: List<Int>): RecyclerView.Adapter<TopicsRecyclerViewAdapter.TopicViewHolder>() {
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
        viewHolder.bindTopic(topic.title, topic.shortDesc, position)
    }

    inner class TopicViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindTopic(name: String, shortDesc: String, position: Int) {
            itemView.txt_topicname.text = name
            itemView.txt_topicshortdesc.text = shortDesc
            itemView.img_icon.setImageResource(topicIcons[position])
            itemView.setOnClickListener{
                onTopicClickedListener?.invoke(position, name)
            }
        }
    }
}