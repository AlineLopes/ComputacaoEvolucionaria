/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atividade1;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author aline
 */
public class Populacao {

    int numeroDeVariaveis;
    int tamPopulacao;
    double interMin, interMax, funcaoObjetivo;
    Individuo melhorIndividuo, piorIndividuo;

    ArrayList<Individuo> individuos = new ArrayList<>();

    //CONSTRUTOR

    public Populacao(int numeroDeVariaveis, int tamPopulacao, double interMin, double interMax) {
        this.numeroDeVariaveis = numeroDeVariaveis;
        this.tamPopulacao = tamPopulacao;
        this.interMin = interMin;
        this.interMax = interMax;
    }

    public Populacao() {
    }
      
    
    //CRIAR POPULACAO
    public void criarPopulacao() {
        individuos = new ArrayList<>();

        for (int i = 0; i < this.getTamPopulacao(); i++) {
            Individuo individuo = new Individuo(interMin, interMax, numeroDeVariaveis);
            individuo.criar();
            individuos.add(individuo);
        }
    }

    //AVALIAR POPULACAO
    public void avaliar() {
        for (Individuo individuo : this.getIndividuos()) {
            //CALCULAR FUNCAO OBJETIVO
            Problema problema = new Problema();
            problema.calcularFuncaoObjetivo(individuo);
        }
    }

    public int getNumeroDeVariaveis() {
        return numeroDeVariaveis;
    }

    public void setNumeroDeVariaveis(int numeroDeVariaveis) {
        this.numeroDeVariaveis = numeroDeVariaveis;
    }

    public int getTamPopulacao() {
        return tamPopulacao;
    }

    public void setTamPopulacao(int tamPopulacao) {
        this.tamPopulacao = tamPopulacao;
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

    public double getFuncaoObjetivo() {
        return funcaoObjetivo;
    }

    public void setFuncaoObjetivo(double funcaoObjetivo) {
        this.funcaoObjetivo = funcaoObjetivo;
    }

    public ArrayList<Individuo> getIndividuos() {
        return individuos;
    }

    public void setIndividuos(ArrayList<Individuo> individuos) {
        this.individuos = individuos;
    }

    public Individuo getMelhorIndividuo() {
        return Collections.min(individuos);
    }

    public void setMelhorIndividuo(Individuo melhorIndividuo) {
        this.melhorIndividuo = melhorIndividuo;
    }

    public Individuo getPiorIndividuo() {
        return Collections.max(individuos);
    }

    public void setPiorIndividuo(Individuo piorIndividuo) {
        this.piorIndividuo = piorIndividuo;
    }
    

}
