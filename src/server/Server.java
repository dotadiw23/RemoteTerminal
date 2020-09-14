package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int PORT;
    private ServerSocket serverSocket;

    Server() {
         PORT = 15432;
    }

    private void startServerSocket() {
        try {
            // Start the server socket
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server deployed");
            startCommunication();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     * An infinite loop that allow the server assign a new thread for each
     * client wants to establish a connection with the server
     */
    private void startCommunication() {
        try {
            while (true) {
                System.out.println("\nWaiting for new clients...");
                Socket socket = serverSocket.accept();
                System.out.println(socket.getInetAddress() + " is connected");
                Runnable newClient = new ClientManagement(socket);
                new Thread(newClient).start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().startServerSocket();
    }
}
