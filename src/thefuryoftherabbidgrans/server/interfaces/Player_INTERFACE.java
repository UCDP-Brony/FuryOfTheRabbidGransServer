/*
 * To change this0 license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server.interfaces;


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
     * inits the player when added to the game.
     * @param id the id of the player.
     * @param game the game in which the player is.
     */
    public void init(int id, Game_INTERFACE game);

    /**
     * called when the Client sends a message.
     * @param message the message.
     */
    public void getMessageFromClient(String message);
    
    /**
     * sends a message to the client.
     * @param message the message.
     */
    public void sendMessageToClient(String message);    

    /**
     * Ends the connection with the client.
     */
    public void endConnection();
}
