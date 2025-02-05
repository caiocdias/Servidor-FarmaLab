/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

public class TimeoutSocketFactory extends RMISocketFactory {
    private final int timeout; // Tempo limite em milissegundos

    public TimeoutSocketFactory(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        socket.setSoTimeout(timeout); // Define o timeout de leitura
        return socket;
    }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }
}

