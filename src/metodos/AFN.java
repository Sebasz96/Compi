/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author sebas
 */
public class AFN {

    Estado EdoIni;
    HashSet<Character> Alfabeto;
    HashSet<Estado> Estados;
    HashSet<Estado> EdosAcept;
    static final char EPSILON = 0x0;

    public AFN() {
        EdoIni = null;
        Alfabeto = new HashSet<>();
        Alfabeto.clear();
        Estados = new HashSet<>();
        Estados.clear();
        EdosAcept = new HashSet<>();
        EdosAcept.clear();
    }
    
    public AFN(char s, int token){
        Estado EdoFin;
        Alfabeto = new HashSet<>();
        Estados = new HashSet<>();
        EdosAcept = new HashSet<>();
        
        EdoIni = new Estado();
        System.out.println("Estado inicial "+EdoIni.IdEdo);
        EdoFin = new Estado();
        System.out.println("Estado final " + EdoFin.IdEdo);
        EdoFin.EdoAcep = token;
        EdoIni.AddTransicion(s, EdoFin);
        
        Alfabeto.add(s);
        Estados.add(EdoIni);
        Estados.add(EdoFin);
        EdosAcept.add(EdoFin);
    }
    
    public AFN AfnBasico(char s, int token){
        Estado EdoFin;
        EdoIni = new Estado();
        EdoFin = new Estado();
        EdoFin.EdoAcep = token;
        EdoIni.AddTransicion(s, EdoFin);
        Alfabeto.add(s);
        Estados.add(EdoIni);
        Estados.add(EdoFin);
        EdosAcept.add(EdoFin);
        return this;
    }
    
    public AFN AfnBasico(char s, char s2, int token){
        char i;
        Estado EdoFin;
        EdoIni = new Estado();
        EdoFin = new Estado();
        EdoFin.EdoAcep = token;
        EdoIni.AddTransicion(s, s2, EdoFin);
        for(i = s; i <= s2; i++)
            Alfabeto.add(i);
        Estados.add(EdoIni);
        Estados.add(EdoFin);
        EdosAcept.add(EdoFin);
        return this;
    }
    
    public AFN UnirAFN(AFN f2){
        Estado NuevoIni = new Estado(), NuevoFin = new Estado();
        NuevoIni.AddTransicion(EPSILON, this.EdoIni);
        NuevoIni.AddTransicion(EPSILON, f2.EdoIni);
        
        for(Estado e : this.EdosAcept){
            e.AddTransicion(EPSILON, NuevoFin);
            NuevoFin.EdoAcep = e.EdoAcep;
            e.EdoAcep = -1;
        }
        for(Estado e : f2.EdosAcept){
            e.AddTransicion(EPSILON, NuevoFin);
            e.EdoAcep = -1;
        }
        this.Estados.add(NuevoIni);
        this.Estados.add(NuevoFin);
        this.EdoIni = NuevoIni;
        this.EdosAcept.clear();
        this.EdosAcept.add(NuevoFin);
        this.Alfabeto.addAll(f2.Alfabeto);
        this.Estados.addAll(f2.Estados);
        f2 = null;
        return this;
    }
    
    public static AFN UnirLexico(HashSet<AFN> afns){
        AFN r = new AFN();
        Estado Nuevoini = new Estado();
        r.EdoIni = Nuevoini;
        r.Estados.add(r.EdoIni);
        for(AFN f : afns){
            System.out.println("Estado Inicial de AFN actual: " + f.EdoIni.IdEdo);
            Nuevoini.AddTransicion(EPSILON, f.EdoIni);
            r.Alfabeto.addAll(f.Alfabeto);
            r.Estados.addAll(f.Estados);
            r.EdosAcept.addAll(f.EdosAcept);
        }
        return r;
    }
    
    public AFN ConcatenarAFN(AFN f2){
        for (Estado e : this.EdosAcept) {
            for (Transicion t : f2.EdoIni.Transiciones)
                e.AddTransicion(t.Minsimb, t.Maxsimb, t.estado);
        }
        f2.Estados.remove(f2.EdoIni);
        for(Estado e : this.EdosAcept){
            e.EdoAcep = -1;
        }
        this.EdosAcept.clear();
        this.EdosAcept.addAll(f2.EdosAcept);
        this.Alfabeto.addAll(f2.Alfabeto);
        this.Estados.addAll(f2.Estados);
        f2 = null;
        return this;
    }
    
    public AFN Opcional(){
        Estado NuevoIni = new Estado(), NuevoFin = new Estado();
        this.Estados.add(NuevoIni);
        this.Estados.add(NuevoFin);
        NuevoIni.AddTransicion(EPSILON, this.EdoIni);
        NuevoIni.AddTransicion(EPSILON, NuevoFin);
        for (Estado e : this.EdosAcept) {
            e.AddTransicion(EPSILON, NuevoFin);
            NuevoFin.EdoAcep = e.EdoAcep;
            e.EdoAcep = -1;
            EdosAcept.remove(e);
        }
        this.EdoIni = NuevoIni;
        EdosAcept.add(NuevoFin);
        return this;
    }
    
    public AFN CerraduraPositiva(){
        Estado NuevoIni = new Estado(), NuevoFin = new Estado();
        this.Estados.add(NuevoIni);
        this.Estados.add(NuevoFin);
        NuevoIni.AddTransicion(EPSILON, this.EdoIni);
        for(Estado e : this.EdosAcept){
            e.AddTransicion(EPSILON, this.EdoIni);
            e.AddTransicion(EPSILON, NuevoFin);
            NuevoFin.EdoAcep = e.EdoAcep;
            e.EdoAcep = -1;
            EdosAcept.remove(e);
        }
        this.EdoIni = NuevoIni;
        EdosAcept.add(NuevoFin);
        return this;
    }
    public AFN CerraduraEstrella(){
        Estado NuevoIni = new Estado(), NuevoFin = new Estado();
        this.Estados.add(NuevoIni);
        this.Estados.add(NuevoFin);
        NuevoIni.AddTransicion(EPSILON, NuevoFin);
        NuevoIni.AddTransicion(EPSILON, this.EdoIni);
        for(Estado e : this.EdosAcept){
            e.AddTransicion(EPSILON, this.EdoIni);
            e.AddTransicion(EPSILON, NuevoFin);
            NuevoFin.EdoAcep = e.EdoAcep;
            e.EdoAcep = -1;
            EdosAcept.remove(e);
        }
        this.EdoIni = NuevoIni;
        EdosAcept.add(NuevoFin);
        return this;
    }
    public HashSet<Estado> CerraduraEpsilon(int id){
        Estado e = BuscarEstado(id);
        if(e == null){
            System.out.println("El estado no existe");
        }
        return CerraduraEpsilon(e);
    }
    public HashSet<Estado> CerraduraEpsilon(Estado e){
        Stack<Estado> Pila = new Stack<>();
        HashSet<Estado> Cerr = new HashSet<>();
        Cerr.clear();
        Pila.push(e);
        char s;
        while(!Pila.empty()){
            e = Pila.pop();
            if(!Cerr.contains(e)){
                Cerr.add(e);
                for(Transicion t : e.Transiciones){
                   for(s = t.Minsimb; s <= t.Maxsimb; s++) 
                       if(s == EPSILON){
                           Pila.push(t.estado);
                       }
                }
            }
        }
        return Cerr;
    }
    
    public HashSet<Estado> Mover(int id, char s){
        Estado edo = BuscarEstado(id);
        HashSet<Estado> R = new HashSet<>();
        R.clear();
        for(Transicion t : edo.Transiciones){
            if(t.Minsimb <= s && s <= t.Maxsimb)
                R.add(t.estado);
        }
        return R;
    }
    
    public HashSet<Estado> IrA(int id, char s){
        HashSet<Estado> R = new HashSet<>();
        R.clear();
        for(Estado e : Mover(id, s)){
            R.addAll(CerraduraEpsilon(e));
        }
        return R;
    }
    
    private Estado BuscarEstado(int id){
        for(Estado e : this.Estados){
            if(id == e.IdEdo){
                return e;
            }
        }
        return null;
    }
    
    public AFD ConvertirAFD(){
        boolean IrAEmpty = false;
        AFD afd = new AFD();
        Queue<HashSet<Estado>> c = new LinkedList();
        Estado inicial = new Estado(0);
        afd.EdoIni = inicial;
        
        afd.Estados.add(inicial);
        
        HashSet<Estado> array_inicial = CerraduraEpsilon(this.EdoIni);
        
        for(Estado aceptacion : this.EdosAcept){
            if(array_inicial.contains(aceptacion)){
                inicial.EdoAcep=aceptacion.EdoAcep;
                afd.EdosAcept.add(inicial);
            }
        }
        
        c.add(array_inicial);
        
        ArrayList<HashSet<Estado>> temporal = new ArrayList();
        
        int indexEstadoInicio = 0;
        
        while(!c.isEmpty()&&!c.peek().isEmpty()){
            
            HashSet<Estado> actual = c.poll();
            
            for(char simbolo : this.Alfabeto){
                
                HashSet<Estado> iraresult = new HashSet<>();
                iraresult.clear();
                
                for(Estado e : actual){
                    iraresult.addAll(IrA(e.IdEdo, simbolo));
                }
                
                Estado anterior = afd.BuscarEstado(indexEstadoInicio);
                
                if(iraresult.isEmpty()){
                }
                
                else if(temporal.contains(iraresult)){
                    Estado siguiente = afd.BuscarEstado(temporal.indexOf(iraresult)+1);
                    anterior.AddTransicion(simbolo, siguiente);
                }
                
                else{
                    temporal.add(iraresult);
                    c.add(iraresult);
                    Estado nuevo = new Estado(temporal.indexOf(iraresult)+1);
                    anterior.AddTransicion(simbolo, nuevo);
                    afd.Estados.add(nuevo);
                    for(Estado aceptacion : this.EdosAcept){
                        if(iraresult.contains(aceptacion)){
                            nuevo.EdoAcep = aceptacion.EdoAcep;
                            afd.EdosAcept.add(nuevo);
                        }
                    }
                }
            }
            indexEstadoInicio++;
        }
        afd.Alfabeto.addAll(Alfabeto);
        return afd;
    }
    
}
