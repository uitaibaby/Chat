package com.company.thirteencourse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ArrayList<Client> clients = new ArrayList<Client>();

    public Server() {
        Socket clientSocket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Standbying");
            while (true) {
                clientSocket = serverSocket.accept();
                Client client = new Client(clientSocket, this);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToAllClients(String message) {
        for (Client o : clients) {
            o.sendMsg(message);
        }
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }
}