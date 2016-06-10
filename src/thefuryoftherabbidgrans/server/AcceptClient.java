/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TheDoctor
 */
public class AcceptClient implements Runnable{
    private final ServerSocket socketServer;
    private Socket socket;
    private Thread t1;
    
    public AcceptClient(ServerSocket s){
        this.socketServer = s;
    }
    
    @Override
    public void run(){
        try {            
            while(true){
                socket = socketServer.accept();
                System.out.println("A client wants to connect.");
                t1 = new Thread(new RoomMatching(socket));
                t1.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(AcceptClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
