package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientManagement implements Runnable {

    private final Socket SOCKET;

    ClientManagement(Socket socket) {
        this.SOCKET = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Input and output channels
                DataInputStream input = new DataInputStream(SOCKET.getInputStream());
                DataOutputStream output = new DataOutputStream(SOCKET.getOutputStream());

                String command = input.readUTF();
                if (command.equals("exit")) {
                    // Close the connection
                    output.writeUTF("CONN_CLOSE");
                    SOCKET.close();
                    break;
                }else {
                    // Execute the command in bash
                    System.out.println("Executing command...");
                    Process terminalProcess = new ProcessBuilder().command("bash", "-c", command).start();

                    BufferedReader commandResult = new BufferedReader(new InputStreamReader(terminalProcess.getInputStream()));

                    String line;
                    StringBuilder terminalOutput = new StringBuilder();
                    // Catch the terminal command output
                    while ((line = commandResult.readLine()) != null) {
                        if (line.length() > 0) {
                            terminalOutput.append(line).append("\n");
                        }
                    }

                    if (terminalOutput.toString().length() > 0){
                        terminalOutput.replace(terminalOutput.length()-1, terminalOutput.length(), ""); // Remove the last \n
                    }
                    output.writeUTF(terminalOutput.toString());
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
