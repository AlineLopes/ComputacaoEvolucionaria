/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodo;

import java.util.ArrayList;
import java.util.Random;
import problema.ProblemaSchwefelsFunction;
import solucao.Individuo;

/**
 *
 * @author aline
 */
public class LAHC {
    int foBest, limite;
    double tx;
    ArrayList<Integer> vetorLAHC;
    problema.ProblemaSchwefelsFunction problema = new ProblemaSchwefelsFunction();
    Individuo trial, xr0;
    
    public LAHC(int foBest, int limite, double tx, ProblemaSchwefelsFunction problema, Individuo trial, Individuo xr0) {
        this.foBest = foBest;
        this.limite = limite;
        this.problema = problema;
        this.tx = tx;
        this.trial = trial;
        this.xr0 = xr0;
    }
    
    public Individuo executar(Individuo ind) throws CloneNotSupportedException{
        Individuo indDescendente;
        Random rnd = new Random();
        if(rnd.nextDouble() <= this.tx){
            vetorLAHC = new ArrayList<>();
            prencherLAHC();
            long t0 = System.currentTimeMillis();
            while(System.currentTimeMillis() - t0 < 1000){
                Individuo indTemp = (Individuo) ind.clone();
                
                indTemp.setFuncaoObjetivo(problema.calcularFuncaoObjetivo(indTemp));
            }
        }
        
    return indDescendente;
    }
    private void prencherLAHC(){
        int cont = 0;
        while(cont < this.limite){
            vetorLAHC.add(foBest);
            cont++;
        }
    }
    
    
}
