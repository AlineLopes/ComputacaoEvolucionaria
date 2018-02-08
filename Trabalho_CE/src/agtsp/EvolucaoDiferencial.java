/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agtsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aline
 */
public class EvolucaoDiferencial {

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

    public EvolucaoDiferencial(int gmax, int D, int Np, double F, double Cr, Problema problema, double taxa) {

        this.gmax = gmax;
        this.D = D;
        this.Np = Np;
        this.F = F;
        this.Cr = Cr;
        this.problema = problema;
        this.taxa = taxa;
    }

    public String executar(int execucao, int caso) throws CloneNotSupportedException {
        int randomNum = 0, cont = 0;
        Random rand = new Random();
        String retorno = "";
        //Criar Populacao Inicial
        Populacao populacao = new Populacao(Np, problema);
        populacao.criar();

        //Avaliacao da Populacao Inicial
        populacao.avaliar();
        try {
            //Recupera Melhor Individuo
            melhorSolucao = (Individuo) populacao.getMelhorIndividuo().clone();
//            piorSolucao = (Individuo) populacao.getPiorIndividuo().clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println("MELHOR E PIOR SOLUÇÃO, ERRO NO CLONE!!");
        }
        //Nova Populacao
        Populacao novaPopulacao = new Populacao(Np, problema);

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

                Individuo trial = new Individuo(problema.dimensao);
                trial.criar();
                Individuo xr0 = (Individuo) populacao.getIndividuos().get(r0);
                Individuo xr1 = (Individuo) populacao.getIndividuos().get(r1);
                Individuo xr2 = (Individuo) populacao.getIndividuos().get(r2);

                Individuo target = new Individuo(problema.dimensao);
                //pertubação
                pertubacao(trial, xr1, xr2);
                //mutação
                mutacao(trial, xr0);

                target = (Individuo) populacao.getIndividuos().get(i);
                Individuo filho1 = new Individuo(problema.dimensao);
                Individuo filho2 = new Individuo(problema.dimensao);
                //crossover
                crossover(trial, target, filho1, filho2);

                //Replace
                filho1 = replace(filho1);
                filho2 = replace(filho2);
                //Seleção
                problema.calcularFuncaoObjetivo(trial);
                problema.calcularFuncaoObjetivo(target);
                problema.calcularFuncaoObjetivo(filho1);
                problema.calcularFuncaoObjetivo(filho2);
                Individuo vetor[] = new Individuo[]{trial, target, filho1, filho2};
                Arrays.sort(vetor);

                novaPopulacao.getIndividuos().add((Individuo) vetor[0].clone());
                novaPopulacao.getIndividuos().add((Individuo) vetor[1].clone());

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
//            if (populacao.getPiorIndividuo().getFuncaoObjetivo() > this.getPiorSolucao().getFuncaoObjetivo()) {
//                try {
//                    this.setPiorSolucao((Individuo) populacao.getPiorIndividuo().clone());
//                } catch (CloneNotSupportedException ex) {
//                    System.out.println("Pior Solução linha 123+/- Erro!");
//                }
//
//            }
//            System.out.println(g + "\n");
        }//fecha g
        retorno += execucao + "," + caso + "," + populacao.getMelhorIndividuo().getFuncaoObjetivo() + "," + (System.currentTimeMillis() - t0);
        System.out.println(populacao.getMelhorIndividuo().getFuncaoObjetivo() + "\n");

//        if (randomNum == 1) {
//            randomNum = 2;
//        } else {
//            randomNum = 1;
//        }
        cont++;
//            System.out.println("RD "+retorno);
        return retorno;
    }

    private void pertubacao(Individuo trial, Individuo xr1, Individuo xr2) throws CloneNotSupportedException {
        //Difrença entre r1 e r2
        ArrayList<Double> diferencas = new ArrayList<>();
        int diferenca;
        for (int i = 0; i < problema.dimensao; i++) {
            for (int j = 0; j < problema.dimensao; j++) {
                if (Objects.equals(xr1.getCromossomos().get(i), xr2.getCromossomos().get(j))) {
                    diferenca = Math.abs(i - j);
                    trial = (Individuo) xr1.clone();
                    int aux = trial.getCromossomos().get(diferenca);
                    trial.getCromossomos().set(diferenca, xr1.getCromossomos().get(i));
                    trial.getCromossomos().set(i, aux);
                }

            }
        }

    }

    private void mutacao(Individuo trial, Individuo xr0) throws CloneNotSupportedException {

        for (int i = 0; i < problema.dimensao; i++) {
            for (int j = 0; j < problema.dimensao; j++) {
                if (Objects.equals(trial.getCromossomos().get(i), xr0.getCromossomos().get(j))) {
                    int aux = trial.getCromossomos().get(j);
                    trial.getCromossomos().set(j, xr0.getCromossomos().get(j));
                    trial.getCromossomos().set(i, aux);
                }

            }
        }
    }

    private void crossover(Individuo pai1, Individuo pai2, Individuo filho1, Individuo filho2) {

        filho1.getCromossomos().addAll(pai1.getCromossomos().subList(0, problema.dimensao / 2));
        filho2.getCromossomos().addAll(pai2.getCromossomos().subList(0, problema.dimensao / 2));

        for (int i = 0; i < problema.dimensao; i++) {
            int ver2 = pai2.getCromossomos().get(i);
            if (!filho1.getCromossomos().contains(ver2)) {
                filho1.getCromossomos().add(ver2);
            }

        }

        for (int i = 0; i < problema.dimensao; i++) {
            int ver1 = pai1.getCromossomos().get(i);
            if (!filho2.getCromossomos().contains(ver1)) {
                filho2.getCromossomos().add(ver1);
            }
        }

    }

    public Individuo replace(Individuo trial) throws CloneNotSupportedException {

        Random rnd = new Random();

        for (int i = 1; i < trial.getCromossomos().size() - 1; i++) {
            if (rnd.nextDouble() <= this.taxa) {
                for (int j = 0; j < getMelhorSolucao().getCromossomos().size(); j++) {
                    if (Objects.equals(trial.getCromossomos().get(i), getMelhorSolucao().getCromossomos().get(j))) {
                        int aux = trial.getCromossomos().get(j);
                        trial.getCromossomos().set(j, trial.getCromossomos().get(i));
                        trial.getCromossomos().set(i, aux);
                        break;
                    }

                }
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
