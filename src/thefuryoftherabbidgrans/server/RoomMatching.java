/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
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

    private String bytesToHex(byte[] b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                         '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
      StringBuilder buf = new StringBuilder();
      for (int j=0; j<b.length; j++) {
         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
         buf.append(hexDigit[b[j] & 0x0f]);
      }
      return buf.toString();
    }

    private String validatePlayer() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, SQLException, NoSuchAlgorithmException, ParserConfigurationException, SAXException {
        String name = "";
        String pass = "";
        while(!validPlayer){
            sendMessage("C210");
            name = in.readLine();
            sendMessage("C211");
            pass = in.readLine();
            File passFile = new File("./src/thefuryoftherabbidgrans/ressources/pass.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(passFile);
            doc.getDocumentElement().normalize();
                                    
            String password = doc.getElementsByTagName("pass").item(0).getTextContent();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gransCommunity","joachim",password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT mot_de_passe FROM gransCommunity.membres WHERE nom_utilisateur='"+name+"';");
            if(rs.next()){
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(pass.getBytes()); 
                byte[] output = md.digest();
                if(rs.getString(1).equals(bytesToHex(output))){
                    this.validPlayer = true;
                    sendMessage("C211_1");
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
