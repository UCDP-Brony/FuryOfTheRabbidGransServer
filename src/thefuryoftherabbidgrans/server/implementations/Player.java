/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server.implementations;

import java.net.Socket;
import thefuryoftherabbidgrans.server.interfaces.Player_INTERFACE;

/**
 *
 * @author TheDoctor
 */
public class Player implements Player_INTERFACE {
    private final Socket socket;
    private String name;
    private int id;

    
    public Player(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void initPlayer(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    
}
