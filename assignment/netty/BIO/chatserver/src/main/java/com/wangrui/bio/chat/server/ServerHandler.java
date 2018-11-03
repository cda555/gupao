package com.wangrui.bio.chat.server;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerHandler implements Runnable {

    private Socket socket;
    //发送给客户端的消息队列
    private Queue<String> sendQueue = new LinkedBlockingQueue<String>();
    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    public boolean add(String message){
        return sendQueue.add(message);
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            String receiveMessage;
            new Thread(new SendMesssage(out)).start();
            while (true){
                receiveMessage = in.readLine();
               if(receiveMessage != null){
                   System.out.println("服务端收到消息："+receiveMessage);
                   ChatServer.linkedBlockingQueue.add(receiveMessage);
               }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SendMesssage implements Runnable{
        private  PrintWriter out = null;

        public SendMesssage(PrintWriter out){
            this.out = out;
        }

        @Override
        public void run() {
            //发送消息
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(int i=0;i <sendQueue.size(); i++){
                    out.println(sendQueue.poll());
                }
            }
        }
    }

}
