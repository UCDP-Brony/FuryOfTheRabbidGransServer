/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TheDoctor
 */
public class TheFuryOfTheRabbidGrans {
    private static InetAddress localAddress;
    private static ServerSocket socketServer;
    private static int portNumber, maxConnections;
    
    public static void main(String[] argv) {
        portNumber = 1500;
        maxConnections = 100;
        try {
            localAddress = InetAddress.getByName("192.168.1.28");   //Server
            //localAddress = InetAddress.getLocalHost();            //Local
            socketServer = new ServerSocket(portNumber, maxConnections, localAddress);
            Thread t = new Thread(new AcceptClient(socketServer));
            t.start();
            System.out.println("Server ready.");                      
        } catch (IOException ex) {
            Logger.getLogger(TheFuryOfTheRabbidGrans.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void close(){
        try {
            socketServer.close();
        } catch (IOException ex) {
            Logger.getLogger(TheFuryOfTheRabbidGrans.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
