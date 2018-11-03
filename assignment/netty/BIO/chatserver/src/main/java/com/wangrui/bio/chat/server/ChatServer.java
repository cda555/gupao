package com.wangrui.bio.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatServer {
    //默认端口
    private static int DEFAULT_PORT = 10000;

    private static ServerSocket serverSocket;

    //服务器端消息队列
    public static Queue<String> linkedBlockingQueue = new LinkedBlockingQueue();

    //线程连接（待会换连接池）
    private static List<ServerHandler> threadList = new ArrayList<ServerHandler>();

    public static void start() throws IOException{
        start(DEFAULT_PORT);
    }

    public static synchronized void start(int defaultPort) {
        if(serverSocket != null) return;
        //将服务端收到的消息队列分发给每个线程
        new Thread(()->{
            String message;
            while (true){
                if(linkedBlockingQueue.size() > 0){
                    message =  linkedBlockingQueue.poll();
                    for(ServerHandler s :threadList){
                        s.add(message);
                    }
                }
            }
        }).start();
        try{
            //
            serverSocket = new ServerSocket(defaultPort);
            System.out.println("服务端已启动，端口号为：" + defaultPort);
            while (true){
                Socket socket =  serverSocket.accept();
                ServerHandler serverHandler = new ServerHandler(socket);
                //怪怪的
                Thread thread = new Thread(serverHandler);
                thread.start();
                threadList.add(serverHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null){
                try {
                    serverSocket.close();
                    serverSocket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }

}
