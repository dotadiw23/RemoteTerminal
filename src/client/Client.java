package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    private final  String HOST;
    private final int PORT;

    Client() {
        HOST = "localhost";
        PORT = 15432;
    }

    private void startCommunication() {
        try {
            // Connect with the socket
            Socket socket = new Socket(HOST, PORT);

            // Input and output channels
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            System.out.println("Welcome to the remote UNIX terminal!\n");


            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("\u001B[32munixterm@" + socket.getRemoteSocketAddress() + "\u001B[0m:\u001B[36;1m~\u001B[0m$ ");
                String command = br.readLine();

                if (command.length() > 0) {
                    output.writeUTF(command);
                    String terminalOutput = input.readUTF();
                    if (terminalOutput.equals("CONN_CLOSE")) {
                        System.out.println("Disconnected by the server");
                        break;
                    }else if (terminalOutput.equals("NO_OUTPUT")) {
                        System.out.println(command + ": command not found");
                    }else {
                        System.out.println(terminalOutput);
                    }
                }
            }

            startCommunication();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client().startCommunication();
    }
}
