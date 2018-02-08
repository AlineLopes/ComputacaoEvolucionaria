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
public class Populacao {

    ArrayList<Individuo> individuos = new ArrayList<>();
    int tamanho;
    Problema problema;

    public Populacao(int tamanho, Problema problema) {
        this.tamanho = tamanho;
        this.problema = problema;
    }

    public ArrayList<Individuo> getIndividuos() {
        return individuos;
    }

    public void setIndividuos(ArrayList<Individuo> individuos) {
        this.individuos = individuos;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public Problema getProblema() {
        return problema;
    }

    public void setProblema(Problema problema) {
        this.problema = problema;
    }

    public void criar() {
        this.individuos = new ArrayList<>();

        for (int i = 1; i <= this.tamanho; i++) {
            Individuo individuo = new Individuo(this.problema.dimensao);
            individuo.criar();
            this.individuos.add(individuo);
        }
    }

    public void avaliar() {
        for (Individuo individuo : this.individuos) {
           this.problema.calcularFuncaoObjetivo(individuo);
        }
    }
    
    public Individuo getMelhorIndividuo(){
        return Collections.min(this.individuos);
    }
    
}
