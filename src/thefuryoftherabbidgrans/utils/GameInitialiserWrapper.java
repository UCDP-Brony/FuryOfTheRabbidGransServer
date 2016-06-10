/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thefuryoftherabbidgrans.utils;

/**
 *
 * @author TheDoctor
 */
public class GameInitialiserWrapper {
    private final String name;
    
    public GameInitialiserWrapper(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
}
