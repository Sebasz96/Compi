/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodos;

/**
 *
 * @author sebas
 */
public class Simbolo {
    private String s;
    private boolean terminal;
    private int token;

    public Simbolo(String s, boolean terminal, int token) {
        this.s = s;
        this.terminal = terminal;
        this.token = token;
    }
    
    

    public String getS() {
        return s;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public int getToken() {
        return token;
    }
    
    
}
