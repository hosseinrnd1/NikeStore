package com.myapp.nikestore.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myapp.nikestore.R
import com.myapp.nikestore.data.Comment
import java.util.ArrayList

class CommentAdapter(val mustShow:Boolean = false): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    var comments=ArrayList<Comment>()
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bindComment(comments[position])
    }

    override fun getItemCount(): Int {
        return if (comments.size>3 && !mustShow) 3 else comments.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentTitleTv=itemView.findViewById<TextView>(R.id.commentTitleTv)
        val commentDateTv=itemView.findViewById<TextView>(R.id.commentDateTv)
        val commentAuthorTv=itemView.findViewById<TextView>(R.id.commentAuthorTv)
        val commentContentTv=itemView.findViewById<TextView>(R.id.commentContent)

        fun bindComment(comment: Comment){
            commentTitleTv.text=comment.title
            commentDateTv.text=comment.date
            commentAuthorTv.text=comment.author.email
            commentContentTv.text=comment.content
        }
    }

}