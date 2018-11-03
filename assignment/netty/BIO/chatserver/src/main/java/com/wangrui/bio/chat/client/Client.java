package com.wangrui.bio.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Client{
    private static int DEFAULT_SERVER_PORT = 10000;
    private static String DEFAULT_SERVER_IP = "127.0.0.1";

    private  Socket socket = null;
    private String name;
    public Client(String name){
        this.name = name;
        try {
            socket = new Socket(DEFAULT_SERVER_IP,DEFAULT_SERVER_PORT);
            System.out.println("服务器连接成功！");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            //发送消息
            new Thread(new SendMessage(out)).start();
            //接收消息
            new Thread(new ReceiveMessage(in)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //发送消息
    class SendMessage implements Runnable{
        private  PrintWriter out = null;
        public SendMessage (PrintWriter out){
            this.out = out;
        }
        @Override
        public void run() {
            Random random = new Random(10000);
            String message;
            while (true){
                try {
                    Thread.sleep(2000);
                    message = name +":" +random.nextInt();
                    out.println(message);
                    System.out.println(name+"发送消息："+message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //接收消息
    class ReceiveMessage implements Runnable{
        private  BufferedReader in  = null;
        public ReceiveMessage (BufferedReader in ){
            this.in = in;
        }
        @Override
        public void run() {
            String message;
            while (true){
                try {
                    message = in.readLine();
                    System.out.println(name+"收到消息："+message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
