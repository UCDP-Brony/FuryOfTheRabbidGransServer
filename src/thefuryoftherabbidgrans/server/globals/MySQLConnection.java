/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.server.globals;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author TheDoctor
 */
public class MySQLConnection {

    private static MySQLConnection instance = null;
    private Connection con;
    
    private MySQLConnection(){
        try {
            //File passFile = new File("./src/thefuryoftherabbidgrans/ressources/pass.xml");    //Local
            File passFile = new File("./thefuryoftherabbidgrans/ressources/pass.xml");          //Server
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(passFile);
            doc.getDocumentElement().normalize();
                                    
            String password = doc.getElementsByTagName("pass").item(0).getTextContent();
            String login = doc.getElementsByTagName("login").item(0).getTextContent();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gransCommunity",login,password);
        } catch (ParserConfigurationException | SAXException | IOException | ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static MySQLConnection getInstance(){
        if(instance == null){
            instance = new MySQLConnection();
        }
        return instance;
    }
    
    public ResultSet execQuery(String q) throws SQLException{
        return this.con.createStatement().executeQuery(q);
    }
    
    public int execUpdate(String u) throws SQLException{
        return this.con.createStatement().executeUpdate(u);
    }

    public ResultSet getPasswordFromUserName(String name) throws SQLException {
        PreparedStatement st = this.con.prepareStatement("SELECT mot_de_passe, connected FROM gransCommunity.membres WHERE nom_utilisateur= ? ;");
        st.setString(1, name);
        st.execute();            
        return st.getResultSet();
    }

    public void updateConnected(String name) throws SQLException {
        PreparedStatement st = this.con.prepareStatement("UPDATE gransCommunity.membres SET connected=1 WHERE nom_utilisateur= ? ;");
        st.setString(1, name);
        st.execute();        
    }
    
}
