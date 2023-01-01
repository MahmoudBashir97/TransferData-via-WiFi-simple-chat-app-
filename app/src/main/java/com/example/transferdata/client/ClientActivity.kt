package com.example.transferdata.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.transferdata.R
import com.example.transferdata.databinding.ActivityClientBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ClientActivity : AppCompatActivity(R.layout.activity_client) {
    lateinit var binding:ActivityClientBinding
    private var thread1=Thread()
     var SOCKET_PORT=0
     var SOCKET_IP =""
    var socket= Socket()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvents()
    }
    private fun initEvents(){
        binding.btnConnect.setOnClickListener {
            SOCKET_IP = binding.etIP.text.toString()
            SOCKET_PORT = binding.etPort.text.toString().toInt()
            Thread(Thread1()).start()
        }
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            if (message.isNotEmpty())
                Thread(Thread3(message)).start()
        }
    }

    private lateinit var output: PrintWriter
    private lateinit var input: BufferedReader
    inner class Thread1:Runnable{
        override fun run() {

            try {
                socket = Socket(SOCKET_IP,SOCKET_PORT)

                    output  = PrintWriter(socket.getOutputStream())
                    input = BufferedReader(InputStreamReader(socket.getInputStream()))
                    runOnUiThread {
                        binding.tvMessages.text = "Connected"
                    }
                    Thread(Thread2()).start()
            }catch (o: IOException){
                o.printStackTrace()
            }

        }

    }

    inner class Thread2: Runnable {

        override fun run() {
            while (true) {
                try {
                    val message = input.readLine();
                    if (message != null) {
                        runOnUiThread {
                            binding.tvMessages.append(
                                "\nserver:$message"
                            )
                        }
                    } else {
                        val Thread1 =  Thread( Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (e: IOException) {
                    e.printStackTrace();
                }
            }
        }
    }
    inner class Thread3: Runnable {
        private var message="";
        constructor(message:String){
            this.message  = message
        }

        override fun run() {
            binding.etMessage.setText("")
            output.println(message)

            output.flush()
//            runOnUiThread{
//                binding.tvMes,sages.append(" client: " + message)
//                binding.etMessage.setText("");
//            }
        }
    }

}