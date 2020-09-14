package server;

import java.io.*;
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

    private void startCommunication() {
        try {
            System.out.println("\nWaiting for new clients...");
            Socket socket = serverSocket.accept();
            System.out.println(socket.getInetAddress() + " is connected");

            while (true) {
                // Input and output channels
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                String command = input.readUTF();
                if (command.equals("exit")) {
                    output.writeUTF("CONN_CLOSE");
                    socket.close();
                    break;
                }else {
                    Process terminalProcess = new ProcessBuilder().command("bash", "-c", command).start();

                    BufferedReader commandResult = new BufferedReader(new InputStreamReader(terminalProcess.getInputStream()));

                    String line;
                    StringBuilder terminalOutput = new StringBuilder();
                    while ((line = commandResult.readLine()) != null) {
                        terminalOutput.append(line).append("\n");
                    }

                    if (terminalOutput.toString().length() > 0){
                        terminalOutput.replace(terminalOutput.length()-1, terminalOutput.length(), ""); // Remove the last \n
                        output.writeUTF(terminalOutput.toString());
                    }else {
                        output.writeUTF("NO_OUTPUT");
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().startServerSocket();
    }
}
