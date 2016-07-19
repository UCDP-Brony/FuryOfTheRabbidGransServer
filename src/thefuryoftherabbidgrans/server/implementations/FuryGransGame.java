/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server.implementations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import thefuryoftherabbidgrans.server.core.Grid;
import thefuryoftherabbidgrans.server.globals.MySQLConnection;
import thefuryoftherabbidgrans.server.interfaces.Game_INTERFACE;
import thefuryoftherabbidgrans.server.interfaces.Player_INTERFACE;

/**
 *
 * @author TheDoctor
 */
public class FuryGransGame implements Game_INTERFACE{
    private final ArrayList<Player_INTERFACE> players;
    private final Grid grid;
    private boolean isRunning, isFinished;
    private int nbTurns;
    /**
     * The room identifier.
     */
    private final String identifier;

    public FuryGransGame(String identifier) {
        this.identifier = identifier;
        this.players = new ArrayList<>();
        this.grid = null;
        this.isRunning = false;
        this.isFinished = false;
        this.nbTurns = 0;
    }
    
    
    
    @Override
    public void launch(){
        this.isRunning = true;
    }

    @Override
    public void playATurn(){
        if(this.isRunning){
            // TODO : play a move
            this.nbTurns++;
            switch (this.checkForWinner()){
                case 1 :
                    System.out.println(players.get(0).getName()+" won !");
                    this.isRunning = false;
                    this.isFinished = true;
                    break;
                case 2 : 
                    System.out.println(players.get(1).getName()+" won !");
                    this.isRunning = false;
                    this.isFinished = true;
                    break;
                default :
                    break;
            }
        } else {
            System.out.println("move played while game not running. What ?");
        }
    }

    private int checkForWinner() {
        return 0;
    }

    @Override
    public boolean canAddPlayer(){
        return(players.size() < 2);
    }
    
    @Override
    public boolean addPlayer(Player_INTERFACE player) {
        if (this.players.isEmpty()) {
            this.players.add(player);
            player.init(0, this);
        } else if (this.players.size() == 1){
            this.players.add(player);
            player.init(1, this);
            players.get(0).sendMessageToClient("C215_P1");
            players.get(0).sendMessageToClient(player.getName());
            players.get(0).sendMessageToClient("C215_P2");
            players.get(0).sendMessageToClient(this.identifier);
            players.get(0).sendMessageToClient("C215_P3");
        } else {
            return false;
        }
        player.sendMessageToClient("C213_P1");
        player.sendMessageToClient(this.identifier);
        player.sendMessageToClient("C213_P2");
        player.sendMessageToClient(String.valueOf(players.size()));
        player.sendMessageToClient("C213_P3");
        return true;
    }

    @Override
    public int getNbPlayers() {
        return players.size();
    }

    @Override
    public void getMessageFromPlayer(int id, String message) {
        if(message.equals("quit")|| message.equals("exit")){
            System.out.println(players.get(id).getName()+" disconnected.");
            String name = players.get(id).getName();            
            players.get(id).serverEndsConnection();
            this.disconnectedPlayer(name);            
        } else {
            players.get(id).sendMessageToClient(message);
        }
    }

    @Override
    public void removePlayer(int id) {
        players.remove(id);
    }    

    @Override
    public void disconnectedPlayer(String name) {        
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getName().equals(name)){
                players.remove(i);
            }
        }
        if(players.size() > 0){
                for(int i = 0; i < players.size(); i++){
                    players.get(i).sendMessageToClient("C218_P1");
                    players.get(i).sendMessageToClient(name);
                    players.get(i).sendMessageToClient("C218_P2");
                    players.get(i).sendMessageToClient(this.identifier);
                    players.get(i).sendMessageToClient("C218_P3");
                    players.get(i).updateId(i);
                }
            }
    }
}
