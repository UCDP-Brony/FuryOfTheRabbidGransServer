/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server.implementations;

import thefuryoftherabbidgrans.server.core.Grid;
import thefuryoftherabbidgrans.server.interfaces.Game_INTERFACE;
import thefuryoftherabbidgrans.server.interfaces.Player_INTERFACE;

/**
 *
 * @author TheDoctor
 */
public class FuryGransGame implements Game_INTERFACE{
    private final Player_INTERFACE[] players;
    private final Grid grid;
    private boolean isRunning, isFinished;
    private int nbTurns;
    private int nbCurrentPlayers;

    public FuryGransGame() {
        this.players = new Player[2];
        this.players[0] = null;
        this.players[1] = null;
        this.grid = null;
        this.isRunning = false;
        this.isFinished = false;
        this.nbTurns = 0;
        this.nbCurrentPlayers = 0;
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
                    System.out.println(players[0].getName()+" won !");
                    this.isRunning = false;
                    this.isFinished = true;
                    break;
                case 2 : 
                    System.out.println(players[1].getName()+" won !");
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
        return(this.nbCurrentPlayers < 2);
    }
    
    @Override
    public boolean addPlayer(Player_INTERFACE player) {
        if (this.players[0] == null) {
            this.players[0] = player;
            player.setId(0);
        } else if (this.players[1] == null){
            this.players[1] = player;
            player.setId(1);
        } else {
            return false;
        }
        this.nbCurrentPlayers++;
        return true;
    }

    @Override
    public int getNbPlayers() {
        return this.nbCurrentPlayers;
    }

    
}
