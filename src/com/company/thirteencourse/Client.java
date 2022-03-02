package com.company.thirteencourse;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
    private Server server;
    private PrintWriter outMessage;
    private Scanner inMessage;
    private Socket clientSocket = null;
    private static int clientsCount = 0;

    public Client(Socket socket, Server server) {
        try {
            clientsCount++;
            this.server = server;
            this.clientSocket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                server.sendMessageToAllClients("We have a new client");
                server.sendMessageToAllClients(clientsCount + " clients online");
                break;
            }

            while (true) {
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    if (clientMessage.equalsIgnoreCase("exit")) {
                        break;
                    }
                    System.out.println(clientMessage);
                    server.sendMessageToAllClients(clientMessage);
                }
            }
        } finally {
            this.close();
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMsg(String message) {
        try {
            outMessage.println(message);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        server.removeClient(this);
        clientsCount--;
        server.sendMessageToAllClients(clientsCount + " clients online");
    }
}
