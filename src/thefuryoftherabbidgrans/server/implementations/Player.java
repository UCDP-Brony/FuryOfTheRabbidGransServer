/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server.implementations;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import thefuryoftherabbidgrans.server.core.PlayerReception;
import thefuryoftherabbidgrans.server.globals.MySQLConnection;
import thefuryoftherabbidgrans.server.interfaces.Game_INTERFACE;
import thefuryoftherabbidgrans.server.interfaces.Player_INTERFACE;

/**
 *
 * @author TheDoctor
 */
public class Player implements Player_INTERFACE {
    private String name;
    private int id;
    private Game_INTERFACE game;
    private PlayerReception reception;
    private final PrintWriter out;
    private final BufferedReader in;

    
    public Player(BufferedReader in, PrintWriter out){
        this.in = in;
        this.out = out;
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
    public void init(int id, Game_INTERFACE game) {
        this.id = id;
        this.game = game;
        reception = new PlayerReception(this, in);
        Thread r = new Thread(reception);
        r.start();
    }

    @Override
    public void getMessageFromClient(String message) {
        this.game.getMessageFromPlayer(id, message);
    }

    @Override
    public void sendMessageToClient(String message) {
        out.println(message);
        out.flush();
    }

    @Override
    public void serverEndsConnection() {
        sendMessageToClient("C219");
        if(game != null)
            game.removePlayer(id);
    }
    
    @Override
    public void clientDisconnected() {
        try {
            MySQLConnection.getInstance().execUpdate("UPDATE gransCommunity.membres SET connected=0 WHERE nom_utilisateur='"+name+"';");
        } catch (SQLException ex) {
            Logger.getLogger(FuryGransGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(name+" disconnected.");
        game.disconnectedPlayer(name);
    }

    @Override
    public void updateId(int i) {
        this.id = i;
    }
}
