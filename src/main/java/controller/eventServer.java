package controller;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class eventServer {
    private static final Executor SERVER_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final int PORT = 9999;

    public static void send(String message) throws IOException, InterruptedException {
        BlockingQueue<String> eventQueue = new ArrayBlockingQueue<>(100);
        SERVER_EXECUTOR.execute(new SteamingServer(eventQueue));
        eventQueue.put(message);    // 将log加入发送列队

    }

    private static class SteamingServer implements Runnable {
        private final BlockingQueue<String> eventQueue;

        public SteamingServer(BlockingQueue<String> eventQueue) {
            this.eventQueue = eventQueue;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(PORT);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);) {
                String last = "";
                String event = eventQueue.take();
                System.out.println("Event: " + event);
                if (!last.equals(event)) {  // 如果前后两次的信息相同（包含时间戳）则不发送
                    last = event;
                    System.out.println(String.format("Writing \"%s\" to the socket.", event));
                    out.println(event);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Server error", e);
            }
        }
    }
}