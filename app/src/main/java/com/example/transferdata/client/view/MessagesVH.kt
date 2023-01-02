package com.example.transferdata.client.view

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.transferdata.client.ClientActivity
import com.example.transferdata.databinding.SingleMessageItemBinding
import com.example.transferdata.server.ServerActivity

class MessagesVH(private val binding:SingleMessageItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(message:String){

            setMessageForClientSide(message)
    }

    private fun setMessageForClientSide(message:String){

        if (message.contains("/client")){
            binding.reciverMessage.text = message.split("/")[0]
            binding.reciverMessage.visibility = View.VISIBLE
        }
        if (message.contains("/server")){
            binding.senderMessage.text = message.split("/")[0]
            binding.senderMessage.visibility = View.VISIBLE
        }
    }
}