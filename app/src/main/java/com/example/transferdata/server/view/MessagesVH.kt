package com.example.transferdata.server.view

import androidx.recyclerview.widget.RecyclerView
import com.example.transferdata.databinding.SingleMessageItemBinding

class MessagesVH(val binding:SingleMessageItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(message:String){
        binding.reciverMessage.text = " $message "
    }
}