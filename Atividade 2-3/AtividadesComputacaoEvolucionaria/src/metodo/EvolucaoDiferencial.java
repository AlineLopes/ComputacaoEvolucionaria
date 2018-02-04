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

    Individuo melhorSolucao, piorSolucao;

    public EvolucaoDiferencial(double min, double max, int gmax, int D, int Np, double F, double Cr, Problema problema) {
        this.min = min;
        this.max = max;
        this.gmax = gmax;
        this.D = D;
        this.Np = Np;
        this.F = F;
        this.Cr = Cr;
        this.problema = problema;
    }

    @Override
    public String executar(int execucao) {
        int cont = 0, randomNum = 0;
        Random rand = new Random();
        String retorno = "";
        //Criação da População inicial
        Populacao populacao = new Populacao(D, Np, min, max);
        populacao.criarPopulacao();
        //Avaliacaod a População Inicial
        populacao.avaliar();
        //NOva População

        //Recuperar melhor Individuo
        try {
            melhorSolucao = (Individuo) populacao.getMelhorIndividuo().clone();
            piorSolucao = (Individuo) populacao.getPiorIndividuo().clone();

        } catch (CloneNotSupportedException ex) {
            System.out.println("MELHOR E PIOR SOLUÇÃO, ERRO NO CLONE!!");
        }

        Populacao novaPopulacao = new Populacao(D, Np, min, max);
        //Enquanto criterio de parada não for atingido
        randomNum = rand.nextInt((2 - 1) + 1) + 1;

        do {
            long t0 = System.currentTimeMillis();

            for (int g = 0; g < gmax; g++) {
                //Para cada vetor da populacao

                for (int i = 0; i < Np; i++) {
                    //Selecionar aleatoriamente
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

                    Individuo target = new Individuo(min, max, randomNum);
                    switch (randomNum) {
                        case 1:
                            pertubacao(trial, xr1, xr2);
                            mutacao(trial, xr0);

                            target = (Individuo) populacao.getIndividuos().get(i);

                            crossover(trial, target);
                            break;
                        case 2:
                            pertubacao(trial, xr1, xr2);
                            mutacaoParcial(trial, xr0);

                            target = (Individuo) populacao.getIndividuos().get(i);

                            crossover(trial, target);
                            break;
                        default:
                            break;
                    }

                    //Selecao
                    problema.calcularFuncaoObjetivo(trial);
                    if (trial.getFuncaoObjetivo() <= target.getFuncaoObjetivo()) {
                        novaPopulacao.getIndividuos().add(trial);
                    } else {
                        try {
                            novaPopulacao.getIndividuos().add((Individuo) target.clone());
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(EvolucaoDiferencial.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }//fecha i
                populacao.getIndividuos().clear();
                populacao.getIndividuos().addAll(novaPopulacao.getIndividuos());
                Collections.sort(populacao.getIndividuos());
                populacao.getIndividuos().subList(this.Np, populacao.getIndividuos().size()).clear();

                if (populacao.getMelhorIndividuo().getFuncaoObjetivo() < this.getMelhorSolucao().getFuncaoObjetivo()) {
                    try {
                        this.setMelhorSolucao((Individuo) populacao.getMelhorIndividuo().clone());
                    } catch (CloneNotSupportedException ex) {
                        System.out.println("Melhor Solução linha 153+/- Erro!");
                    }
                }

                if (populacao.getPiorIndividuo().getFuncaoObjetivo() > this.getPiorSolucao().getFuncaoObjetivo()) {
                    try {
                        this.setPiorSolucao((Individuo) populacao.getPiorIndividuo().clone());
                    } catch (CloneNotSupportedException ex) {
                        System.out.println("Pior Solução linha 156+/- Erro!");
                    }
                }

//            System.err.println(execucao + "," + populacao.getMelhorIndividuo().getFuncaoObjetivo());
            }
            retorno += execucao + "," + randomNum + "," + populacao.getMelhorIndividuo().getFuncaoObjetivo() + "," + (System.currentTimeMillis() - t0) + "\n";

            if (randomNum == 1) {
                randomNum = 2;
            } else {
                randomNum = 1;
            }
            cont++;
        } while (cont < 2);
        return retorno;
    }

    private void pertubacao(Individuo trial, Individuo xr1, Individuo xr2) {
        //Diferenca entre r1 e r2
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

    private void mutacao(Individuo trial, Individuo xr0) {
        //Multiplicar por F a diferenca e somar com xr0
        double valor;
        for (int i = 0; i < D; i++) {
            do {
                valor = xr0.getCromossomos().get(i) + this.F * (trial.getCromossomos().get(i));
                trial.getCromossomos().set(i, valor);
                if (valor < this.getMin() || valor > this.getMax()) {
                    valor = -xr0.getCromossomos().get(i) + this.F * (trial.getCromossomos().get(i));
                    trial.getCromossomos().set(i, valor);
                }
            } while (valor < this.getMin() || valor > this.getMax());

        }

    }

    private void mutacaoParcial(Individuo trial, Individuo xr0) {
        //Multiplicar por F a diferenca e somar com xr0
        double valor;
        for (int i = 0; i < D / 2; i++) {
            do {
                valor = xr0.getCromossomos().get(i) + this.F * (trial.getCromossomos().get(i));
                trial.getCromossomos().set(i, valor);
                if (valor < this.getMin() || valor > this.getMax()) {
                    valor = -xr0.getCromossomos().get(i) + this.F * (trial.getCromossomos().get(i));
                    trial.getCromossomos().set(i, valor);
                }
            } while (valor < this.getMin() || valor > this.getMax());

        }

    }

    private void crossover(Individuo trial, Individuo target) {
        Random rnd = new Random();
        int jRand = rnd.nextInt(D);

        for (int i = 0; i < this.D; i++) {
            if (!(rnd.nextDouble() <= this.Cr || i == jRand)) {
                trial.getCromossomos().set(i, target.getCromossomos().get(i));
            }

        }
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

}
