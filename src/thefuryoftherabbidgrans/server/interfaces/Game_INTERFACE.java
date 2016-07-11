/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server.interfaces;

/**
 *
 * @author TheDoctor
 */
public interface Game_INTERFACE{    

    /**
     * launches the game.
     */
    public void launch();

    /**
     * plays a turn.
     */
    public void playATurn();

    /**
     * adds a player to the game
     * @param player the player to add.
     * @return true if player could join the game. False if game already full.
     */
    public boolean addPlayer(Player_INTERFACE player);
    
    /**
     * tells if a player can be added.
     * @return true if there is still room for a player, false otherwise.
     */
    public boolean canAddPlayer();
        
    /**
     * get how many players are currently registered in the game.
     * @return the current number of players (0, 1 or 2).
     */
    public int getNbPlayers();    

    /**
     * gets a message from the player id.
     * @param id the id of the player.
     * @param message the message.
     */
    public void getMessageFromPlayer(int id, String message);

    /**
     * removes a player from the game.
     * @param id the id of the player to remove.
     */
    public void removePlayer(int id);
    
    public void disconnectedPlayer(String name);
}
