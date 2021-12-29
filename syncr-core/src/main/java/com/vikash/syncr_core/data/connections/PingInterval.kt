package com.vikash.syncr_core.data.connections;

import java.util.*

class PingInterval(private val socketService: SocketService) : Runnable{
    private var running = true;
    var startTime = System.currentTimeMillis()
    override fun run() {
        while(running){
            Thread.sleep(5000)
            startTime = System.currentTimeMillis()
            socketService.send("pong")
        }
    }

    fun isRunning() = running

    fun stop(){
        running = false
    }
}
