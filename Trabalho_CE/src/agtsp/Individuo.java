/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agtsp;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author aline
 */
public class Individuo implements Comparable<Individuo> {

    //Representação ~deinir a ordem de visitação das cidades
    private ArrayList<Integer> cromossomos;
    private Double funcaoObjetivo;
    private Integer dimensao;

    public Individuo(Integer dimensao) {
        this.dimensao = dimensao;
        this.cromossomos = new ArrayList<>();
    }

    public ArrayList<Integer> getCromossomos() {
        return cromossomos;
    }

    public void setCromossomos(ArrayList<Integer> cromossomos) {
        this.cromossomos = cromossomos;
    }

    public Double getFuncaoObjetivo() {
        return funcaoObjetivo;
    }

    public void setFuncaoObjetivo(Double funcaoObjetivo) {
        this.funcaoObjetivo = funcaoObjetivo;
    }

    public void criar() {
        this.cromossomos = new ArrayList<>();
        for (int i = 1; i <= this.dimensao; i++) {
            this.cromossomos.add(i);
        }
        Collections.shuffle(this.cromossomos);
    }

    @Override
    public int compareTo(Individuo o) {
        return this.getFuncaoObjetivo().compareTo(o.getFuncaoObjetivo());
    }

    @Override
    public String toString() {
        return "Individuo{" + "cromossomos=" + cromossomos + ", funcaoObjetivo=" + funcaoObjetivo + ", dimensao=" + dimensao + '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Individuo individuo = new Individuo(dimensao);
        individuo.setCromossomos(new ArrayList<>(this.getCromossomos()));
        individuo.setFuncaoObjetivo(this.getFuncaoObjetivo());
        
        return individuo;
    }

    

    
}
