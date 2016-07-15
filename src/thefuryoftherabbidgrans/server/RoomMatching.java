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
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import thefuryoftherabbidgrans.server.globals.MySQLConnection;
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
    private boolean roomed, validPlayer;
    private Thread t2;
    
    public RoomMatching(Socket socket) {
        this.socket = socket;
        this.out = null;
        this.in = null;
        this.room = null;
        this.roomed = false;
        this.validPlayer = false;
    }

    
    @Override
    public void run() {
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            
            Player_INTERFACE p = new Player(in, out);
            String name = validatePlayer();            
            p.initPlayer(name);
            roomPlayer(p);                        
            
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchAlgorithmException ex) {
            sendMessage("C500");
        } catch (ParserConfigurationException | SQLException | SAXException ex) {
            Logger.getLogger(RoomMatching.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendMessage(String s){
        out.println(s);
        out.flush();
    }

    private String validatePlayer() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, SQLException, NoSuchAlgorithmException, ParserConfigurationException, SAXException {
        String name = "";
        String pass = "";
        while(!validPlayer){
            sendMessage("C210");
            name = in.readLine();
            sendMessage("C211");
            pass = in.readLine();            
            ResultSet rs = MySQLConnection.getInstance().execQuery("SELECT mot_de_passe, connected FROM gransCommunity.membres WHERE nom_utilisateur='"+name+"';");
            if(rs.next()){                
                if(rs.getString(1).equals(pass)){
                    if(Integer.parseInt(rs.getString(2))==0){                  
                        MySQLConnection.getInstance().execUpdate("UPDATE gransCommunity.membres SET connected=1 WHERE nom_utilisateur='"+name+"';");
                        this.validPlayer = true;                    
                        sendMessage("C211_1");
                    } else {
                        sendMessage("C411_1");
                    }
                } else {
                    sendMessage("C411");
                }
            }
        }
        return name;
    }
    
    private void roomPlayer(Player_INTERFACE p) throws IOException{
        RoomManager rm = RoomManager.getInstance();
        while (!roomed){
            sendMessage("C212");
            room = in.readLine();
            if(rm.roomExists(room)){
                if(rm.canAddToRoom(room)){
                    rm.addPlayerToRoom(room, p);
                    roomed = true;
                } else {
                    sendMessage("C413");
                    roomed = false;
                }
            }else{
                rm.addEntry(room);
                rm.addPlayerToRoom(room, p);
                roomed = true;
            } 
        }
        System.out.println(p.getName()+" joined the room "+room+". There are "+rm.getRoomByID(room).getNbPlayers()+" players in the room.");
    }
}
