/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server;

import java.util.ArrayList;
import thefuryoftherabbidgrans.server.implementations.FuryGransGame;
import thefuryoftherabbidgrans.server.interfaces.Game_INTERFACE;
import thefuryoftherabbidgrans.server.interfaces.Player_INTERFACE;

/**
 *
 * @author TheDoctor
 */
public class RoomManager {
    private static final ArrayList<String> ROOMS_IDS = new ArrayList<>();
    private static final ArrayList<Game_INTERFACE> GAMES = new ArrayList<>();
    
    public static  Game_INTERFACE getRoomByID(String id){
        try{
            return(GAMES.get(ROOMS_IDS.indexOf(id)));
        } catch (IndexOutOfBoundsException ex){
            return null;
        }
    }
    
    public static boolean addEntry(String id){
        if (ROOMS_IDS.contains(id))
            return false;
        ROOMS_IDS.add(id);
        GAMES.add(new FuryGransGame(id));
        return true;
    }
    
    public static boolean addPlayerToRoom(String id, Player_INTERFACE player){
        Game_INTERFACE game = RoomManager.getRoomByID(id);
        if(game == null)
            return false;
        return game.addPlayer(player);
    }

    public static boolean canAddToRoom(String id) {
        try{
            return(GAMES.get(ROOMS_IDS.indexOf(id)).canAddPlayer());
        }
        catch (IndexOutOfBoundsException ex){
            return false;
        }
    }

    static boolean roomExists(String room) {
        return (ROOMS_IDS.contains(room));
    }
}
