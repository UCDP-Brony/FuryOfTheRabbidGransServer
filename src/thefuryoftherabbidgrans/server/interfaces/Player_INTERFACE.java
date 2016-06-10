/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server.interfaces;

import java.net.Socket;


/**
 *
 * @author TheDoctor
 */
public interface Player_INTERFACE{

    /**
     * get the player's name.
     * @return the player's name.
     */
    public String getName();
    
    /**
     * inits player's components
     * @param name
     */
    public void initPlayer(String name);

    /**
     * gets player's id.
     * @return the id of the player.
     */
    public int getId();
    
    /**
     * set's the id of the player.
     * @param id the new id of the player.
     */
    public void setId(int id);    
}
