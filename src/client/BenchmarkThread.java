package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class BenchmarkThread implements Runnable{

    private final  String HOST;
    private final int PORT;
    private final String command;

    BenchmarkThread(String host, int port, String command) {
        HOST = host;
        PORT = port;
        this.command = command;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(HOST, PORT);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(command);
            String terminalOutput = input.readUTF();

            if (terminalOutput.equals("NO_OUTPUT")) {
                System.out.println(command + ": command not found");
            }else {
                System.out.println(terminalOutput);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
