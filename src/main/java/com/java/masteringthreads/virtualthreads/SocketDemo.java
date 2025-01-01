package com.java.masteringthreads.virtualthreads;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketDemo {

    protected static final int PORT = 8082;

    public static void main(String[] args) throws IOException, InterruptedException {
//        Thread.ofVirtual().start(() -> {
//            ServerSocket serverSocket = null;
//            try {
//                serverSocket = new ServerSocket(8082);
//                Socket socket = serverSocket.accept();
//                InputStream inputStream = socket.getInputStream();
//                System.out.println("Reading from the socket with : " +Thread.currentThread().getName());
//                int read = inputStream.read();
//                System.out.println("Read : " +read);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).join();

        Thread.ofPlatform().start(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                while (true) {
                    Socket socket = serverSocket.accept();
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        Thread platform = Thread.ofPlatform().start(SocketDemo::readFromSocket);
        Thread virtual = Thread.ofVirtual().start(SocketDemo::readFromSocket);

        Thread.sleep(2000);
        platform.interrupt();
        virtual.interrupt();
    }

    private static void readFromSocket() {
        try {
            Socket socket = new Socket("localhost", PORT);
            InputStream in = socket.getInputStream();
            System.out.println("Reading from socket with " + Thread.currentThread());
            int read = in.read();
            System.out.println("read = " + read);
        } catch (IOException e) {
            System.out.println("Socket closed via " + e + " on " + Thread.currentThread());
        }
    }
}
