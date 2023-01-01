package com.example.transferdata.server.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.transferdata.databinding.SingleMessageItemBinding

class MessagesAdapter: RecyclerView.Adapter<MessagesVH>() {

    private var messagesList:ArrayList<String> = ArrayList()
    fun setList(mList:ArrayList<String>){
        this.messagesList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesVH {
        return MessagesVH(SingleMessageItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: MessagesVH, position: Int) {
        holder.bind(message = messagesList[position])
    }

    override fun getItemCount(): Int = messagesList.size


}