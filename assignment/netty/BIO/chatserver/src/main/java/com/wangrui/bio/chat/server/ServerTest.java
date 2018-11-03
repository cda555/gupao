package com.wangrui.bio.chat.server;

import java.io.IOException;

public class ServerTest {

    public static void main(String[] args) {
        try {
            ChatServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
