/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import problema.Problema;
import solucao.Individuo;
import solucao.Populacao;

/**
 *
 * @author aline
 */
public class EvolucaoDiferencial implements Metodo {

    private double min, max;
    private int gmax;//criterio de parada
    private int D;//numero de variaveis
    private int Np; //tamanho da população
    private double F;//coeficiente de mutacao
    private double Cr; //coeficiente de crossover
    private Problema problema; // Rastrigin
    //LAHC
    private int limite, timeout;
    private double taxa;
    Individuo melhorSolucao, piorSolucao;

    public EvolucaoDiferencial(double min, double max, int gmax, int D, int Np, double F, double Cr, problema.ProblemaSchwefelsFunction problema, double taxa) {
        this.min = min;
        this.max = max;
        this.gmax = gmax;
        this.D = D;
        this.Np = Np;
        this.F = F;
        this.Cr = Cr;
        this.problema = problema;
        this.taxa = taxa;
    }

    @Override
    public String executar( int execucao, int caso) {
        int randomNum = 0, cont = 0;
        Random rand = new Random();
        String retorno = "";
        //Criar Populacao Inicial
        Populacao populacao = new Populacao(D, Np, min, max);
        populacao.criarPopulacao();
        //Avaliacao da Populacao Inicial
        populacao.avaliar();
        System.out.println("População Inicial: "+ populacao.getFuncaoObjetivo());
        try {
            //Recupera Melhor Individuo
            melhorSolucao = (Individuo) populacao.getMelhorIndividuo().clone();
            piorSolucao = (Individuo) populacao.getPiorIndividuo().clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println("MELHOR E PIOR SOLUÇÃO, ERRO NO CLONE!!");
        }
        //Nova Populacao
        Populacao novaPopulacao = new Populacao(D, Np, min, max);

//        randomNum = rand.nextInt((2 - 1) + 1) + 1;

        //Enquanto o criterio de parada não for atingido
        long t0 = System.currentTimeMillis();

        for (int g = 0; g < this.gmax; g++) {//geracoes
            for (int i = 0; i < Np; i++) {//populacao
                Random rnd = new Random();
                int r0 = rnd.nextInt(Np);
                int r1, r2;
                do {
                    r1 = rnd.nextInt(Np);
                } while (r1 == r0);

                do {
                    r2 = rnd.nextInt(Np);
                } while (r2 == 1 || r2 == r0);

                Individuo trial = new Individuo(min, max, D);
                trial.criar();
                Individuo xr0 = (Individuo) populacao.getIndividuos().get(r0);
                Individuo xr1 = (Individuo) populacao.getIndividuos().get(r1);
                Individuo xr2 = (Individuo) populacao.getIndividuos().get(r2);

                Individuo target = new Individuo(min, max, D);
                //pertubação
                pertubacao(trial, xr1, xr2);
                //mutação
                try {
                    trial = (Individuo) mutacao(trial, xr0).clone();
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(EvolucaoDiferencial.class.getName()).log(Level.SEVERE, null, ex);
                }
                target = (Individuo) populacao.getIndividuos().get(i);
                //crossover
                crossover(trial, target);
                try {
                    //replace
                    trial = (Individuo) replace(trial).clone();
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(EvolucaoDiferencial.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Seleção
                problema.calcularFuncaoObjetivo(trial);
                if (trial.getFuncaoObjetivo() <= target.getFuncaoObjetivo()) {
                    try {
                        novaPopulacao.getIndividuos().add((Individuo) trial.clone());
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(EvolucaoDiferencial.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        novaPopulacao.getIndividuos().add((Individuo) target.clone());
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(EvolucaoDiferencial.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
//                System.out.println(i + "\n");
            }//fecha i
//            populacao.getIndividuos().clear();
            populacao.getIndividuos().addAll(novaPopulacao.getIndividuos());
            novaPopulacao.getIndividuos().clear();
            Collections.sort(populacao.getIndividuos());
            populacao.getIndividuos().subList(this.Np, populacao.getIndividuos().size()).clear();

            if (populacao.getMelhorIndividuo().getFuncaoObjetivo() < this.getMelhorSolucao().getFuncaoObjetivo()) {
                try {
                    this.setMelhorSolucao((Individuo) populacao.getMelhorIndividuo().clone());
                } catch (CloneNotSupportedException ex) {
                    System.out.println("Melhor Solução linha 114+/- Erro!");
                }
            }
            if (populacao.getPiorIndividuo().getFuncaoObjetivo() > this.getPiorSolucao().getFuncaoObjetivo()) {
                try {
                    this.setPiorSolucao((Individuo) populacao.getPiorIndividuo().clone());
                } catch (CloneNotSupportedException ex) {
                    System.out.println("Pior Solução linha 123+/- Erro!");
                }

            }
//            System.out.println(g + "\n");
        }//fecha g
        retorno += execucao+","+caso+","+populacao.getMelhorIndividuo().getFuncaoObjetivo() + "," + (System.currentTimeMillis() - t0) + "\n";
        System.out.println(populacao.getMelhorIndividuo().getFuncaoObjetivo()+"\n"); 
        
//        if (randomNum == 1) {
//            randomNum = 2;
//        } else {
//            randomNum = 1;
//        }
        cont++;
//            System.out.println("RD "+retorno);
        return retorno;
    }

    private void pertubacao(Individuo trial, Individuo xr1, Individuo xr2) {
        //Difrença entre r1 e r2
        ArrayList<Double> diferencas = new ArrayList<>();
        double diferenca;
        for (int i = 0; i < D; i++) {
            do {
                diferenca = xr1.getCromossomos().get(i) - xr2.getCromossomos().get(i);
                trial.getCromossomos().set(i, diferenca);
                if (diferenca < this.getMin() || diferenca > this.getMax()) {
                    diferenca = -xr1.getCromossomos().get(i) - xr2.getCromossomos().get(i);
                    trial.getCromossomos().set(i, diferenca);
                }

            } while (diferenca < this.getMin() || diferenca > this.getMax());
        }

    }

    private Individuo mutacao(Individuo trial, Individuo xr0) throws CloneNotSupportedException {
        //Multiplicar por F a diferenca e somar com xr0
        Individuo trialM = (Individuo) trial.clone();
        double valor;
        for (int i = 0; i < D; i++) {
            do {
                valor = xr0.getCromossomos().get(i) + this.F * (trial.getCromossomos().get(i));
                trialM.getCromossomos().set(i, valor);
                if (valor < this.getMin() || valor > this.getMax()) {
                    valor = -xr0.getCromossomos().get(i) + this.F * (trial.getCromossomos().get(i));
                    trialM.getCromossomos().set(i, valor);
                }

            } while (valor < this.getMin() || valor > this.getMax());
        }
        return trialM;
    }

    private void crossover(Individuo trial, Individuo target) {
        Random rnd = new Random();
        int jRand = rnd.nextInt();

        for (int i = 0; i < this.D; i++) {
            if (!(rnd.nextDouble() <= this.Cr || i == jRand)) {
                trial.getCromossomos().set(i, target.getCromossomos().get(i));
            }
        }
    }

    public Individuo replace(Individuo trial) throws CloneNotSupportedException {

        Random rnd = new Random();

        for (int i = 1; i < trial.getCromossomos().size() - 1; i++) {
            if (rnd.nextDouble() <= this.taxa) {

                trial.getCromossomos().set(i, this.getMelhorSolucao().getCromossomos().get(i));

            }
        }
        return trial;
    }

    public void prencherLAHC(ArrayList vetorLAHC, double foBest) {
        int cont = 0;
        while (cont < this.limite) {
            vetorLAHC.add(foBest);
            cont++;
        }
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public int getGmax() {
        return gmax;
    }

    public void setGmax(int gmax) {
        this.gmax = gmax;
    }

    public int getD() {
        return D;
    }

    public void setD(int D) {
        this.D = D;
    }

    public int getNp() {
        return Np;
    }

    public void setNp(int Np) {
        this.Np = Np;
    }

    public double getF() {
        return F;
    }

    public void setF(double F) {
        this.F = F;
    }

    public double getCr() {
        return Cr;
    }

    public void setCr(double Cr) {
        this.Cr = Cr;
    }

    public Problema getProblema() {
        return problema;
    }

    public void setProblema(Problema problema) {
        this.problema = problema;
    }

    public Individuo getMelhorSolucao() {
        return melhorSolucao;
    }

    public void setMelhorSolucao(Individuo melhorSolucao) {
        this.melhorSolucao = melhorSolucao;
    }

    public Individuo getPiorSolucao() {
        return piorSolucao;
    }

    public void setPiorSolucao(Individuo piorSolucao) {
        this.piorSolucao = piorSolucao;
    }

}
