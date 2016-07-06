/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import thefuryoftherabbidgrans.server.implementations.Player;
import thefuryoftherabbidgrans.server.interfaces.Player_INTERFACE;

/**
 *
 * @author TheDoctor
 */
class RoomMatching implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String room;
    private boolean roomed;
    private Thread t2;
    
    public RoomMatching(Socket socket) {
        this.socket = socket;
        this.out = null;
        this.in = null;
        this.room = null;
        this.roomed = false;
    }

    @Override
    public void run() {
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            
            Player_INTERFACE p = new Player(in, out);
            
            sendMessage("Enter player name !");
            String name = in.readLine();            
            p.initPlayer(name);
            RoomManager rm = RoomManager.getInstance();
            while (!roomed){
                sendMessage("Enter room identifier !");
                room = in.readLine();
                if(rm.roomExists(room)){
                    if(rm.canAddToRoom(room)){
                        rm.addPlayerToRoom(room, p);
                        roomed = true;
                    } else {
                        sendMessage("Room already full. Please enter another room identifier.");
                        roomed = false;
                    }
                }else{
                    rm.addEntry(room);
                    rm.addPlayerToRoom(room, p);
                    roomed = true;
                } 
            }
            System.out.println(p.getName()+" joined the room "+room+". There are "+rm.getRoomByID(room).getNbPlayers()+" players in the room.");
        } catch (IOException ex) {
            Logger.getLogger(RoomMatching.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendMessage(String s){
        out.println(s);
        out.flush();
    }
}
