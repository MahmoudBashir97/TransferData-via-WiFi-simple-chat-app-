package com.example.transferdata.server

import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.transferdata.R
import com.example.transferdata.databinding.ActivityMainBinding
import com.example.transferdata.server.view.MessagesAdapter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.UnknownHostException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ServerActivity : AppCompatActivity(R.layout.activity_main) {
    lateinit var binding: ActivityMainBinding
    lateinit var serverSocket:ServerSocket
    val thread = Thread()
    var isSent=false
    companion object{
        var SERVER_PORT=8080
        var SERVER_IP=""
        var messagesList:ArrayList<String> = ArrayList()
    }
    var message:String=""
    lateinit var mAdapter:MessagesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerview()

        attachIp()
        initEvents()

        Thread(Thread1()).start()
    }

    private fun setupRecyclerview(){
        messagesList = ArrayList()
        mAdapter = MessagesAdapter()
        binding.recMessages.apply {
            setHasFixedSize(true)
            adapter= mAdapter
        }
    }


    private fun initEvents(){
        binding.btnSend.setOnClickListener {
            message = binding.etMessage.text.toString()
            if (message.isNotEmpty()){
                Thread(Thread1()).start()
                Thread(Thread3(message)).start()
                if (isSent) {
                    messagesList.add("$message/server")
                    mAdapter.setList(messagesList)
                }
            }
        }
    }
    private fun attachIp(){
        try {
            SERVER_IP = getLocalIpAddress()
        }catch (e:UnknownHostException){
            e.message
        }
    }

    private fun getLocalIpAddress(): String {
        val wifiMang = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiMang.connectionInfo
        val ipInt = wifiInfo.ipAddress
        return InetAddress.getByAddress(
            ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()
        ).hostAddress
    }

    private lateinit var output:PrintWriter
    private lateinit var input:BufferedReader
    inner class Thread1:Runnable{
        override fun run() {
            var socket=Socket()
            try {
                serverSocket = ServerSocket(SERVER_PORT)
                runOnUiThread {
                    binding.tvMessages.text = "Not Connected"
                    binding.tvIP.text = "IP : $SERVER_IP"
                    binding.tvPort.text = "PORT : $SERVER_PORT"
                }

                try {
                    socket = serverSocket.accept()
                    output  = PrintWriter(socket.getOutputStream())
                    input = BufferedReader(InputStreamReader(socket.getInputStream()))
                    runOnUiThread {
                        binding.tvMessages.text = "Connected"
                    }
                    if (input.readLine() != null) Thread(Thread2()).start()
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }catch (o:IOException){
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
//                            binding.tvMessages.append(
//                                "\nclient:$message"
//                            )
                            messagesList.add(message)
                            mAdapter.setList(messagesList)
                            Log.d("?????","Mmessage : ${messagesList.size}")
                        }
                    } else {
                        val Thread1 =  Thread( Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (e:IOException) {
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
            output.println("$message/server")
            isSent=true
            output.flush();
//            runOnUiThread{
//                    binding.tvMessages.append("server: " + message)
//                    binding.etMessage.setText("");
//            }
        }
    }
}