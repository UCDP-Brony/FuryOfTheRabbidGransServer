/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import thefuryoftherabbidgrans.server.interfaces.Player_INTERFACE;

/**
 *
 * @author TheDoctor
 */
public class PlayerReception implements Runnable {
    private final Player_INTERFACE player;
    private BufferedReader in;
    private boolean connected;
    
    public PlayerReception(Player_INTERFACE p, BufferedReader in){
        this.player = p;
        this.in = in;
        this.connected = true;
    }

    @Override
    public void run() {
        while(connected){
            try{
                String message = in.readLine();
                player.getMessageFromClient(message);
            } catch (IOException ex) {
                connected = false;
            }
        }
    }    
}
