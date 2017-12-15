/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atividade1;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author aline
 */
public class Individuo implements Comparable<Individuo> {

    private ArrayList<Double> cromossomos; //GENOTIPO
    Double funcaoObjetivo;
    double interMin, interMax;
    int numeroDeVariaveis;

    public Individuo(double interMin, double interMax, int numeroDeVariaveis) {
        this.interMin = interMin;
        this.interMax = interMax;
        this.numeroDeVariaveis = numeroDeVariaveis;
    }

    public ArrayList<Double> getCromossomos() {
        return cromossomos;
    }

    public void setCromossomos(ArrayList<Double> cromossomos) {
        this.cromossomos = cromossomos;
    }

    public Double getFuncaoObjetivo() {
        return funcaoObjetivo;
    }

    public void setFuncaoObjetivo(Double funcaoObjetivo) {
        this.funcaoObjetivo = funcaoObjetivo;
    }

    public double getInterMin() {
        return interMin;
    }

    public void setInterMin(double interMin) {
        this.interMin = interMin;
    }

    public double getInterMax() {
        return interMax;
    }

    public void setInterMax(double interMax) {
        this.interMax = interMax;
    }

    public int getNumeroDeVariaveis() {
        return numeroDeVariaveis;
    }

    public void setNumeroDeVariaveis(int numeroDeVariaveis) {
        this.numeroDeVariaveis = numeroDeVariaveis;
    }

    public void criar() {

        this.cromossomos = new ArrayList<>();
        double valor;
        Random rnd = new Random();

        for (int i = 0; i < this.getNumeroDeVariaveis(); i++) {
            valor = this.getInterMin() + (this.getInterMax() - this.getInterMin()) * rnd.nextDouble();
            this.cromossomos.add(valor);
        }

    }

    @Override
    public String toString() {
        return "Individuo{" + "cromossomos=" + cromossomos + ", funcaoObjetivo=" + funcaoObjetivo + ", interMin=" + interMin + ", interMax=" + interMax + ", numeroDeVariaveis=" + numeroDeVariaveis + '}';
    }

    @Override
    public int compareTo(Individuo o) {
        return this.getFuncaoObjetivo().compareTo(o.getFuncaoObjetivo());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Individuo individuo = null;
        individuo = new Individuo(this.getInterMin(), this.getInterMax(), this.getNumeroDeVariaveis());
        individuo.setCromossomos(new ArrayList<>(this.getCromossomos()));
        
        individuo.setFuncaoObjetivo(this.getFuncaoObjetivo());
    
        return individuo;
    }

    
    
}
