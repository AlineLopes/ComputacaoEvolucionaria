/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodo;

import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import problema.Problema;
import problema.ProblemaRastringin;
import solucao.Individuo;
import solucao.Populacao;

/**
 *
 * @author aline
 */
public class EstrategiaEvolucionaria implements Metodo {

    int geracoes, numeroDeVariaveis, mu, lambda;
    double taxMutacao, interMin, interMax;
    Individuo melhorSolucao, piorSolucao;

    private Problema problema;

    public EstrategiaEvolucionaria(int geracoes, int numeroDeVariaveis, int mu, int lambda, double taxMutacao, double interMin, double interMax, Problema problema) {
        this.geracoes = geracoes;
        this.numeroDeVariaveis = numeroDeVariaveis;
        this.mu = mu;
        this.lambda = lambda;
        this.taxMutacao = taxMutacao;
        this.interMin = interMin;
        this.interMax = interMax;
        this.problema = problema;
    }

    @Override
    public String executar(int execucao) {
        Random rand = new Random();
        int randomNum = 0, cont = 0;
        String retorno = "";

        Populacao populacao = new Populacao(numeroDeVariaveis, mu, interMin, interMax);
        populacao.criarPopulacao();

        //Avaliar
        populacao.avaliar();

        //Recuperar melhor Individuo
        try {
            melhorSolucao = (Individuo) populacao.getMelhorIndividuo().clone();
            piorSolucao = (Individuo) populacao.getPiorIndividuo().clone();

        } catch (CloneNotSupportedException ex) {
            System.out.println("MELHORE E PIOR SOLUÇÃO, ERRO NO CLONE!!");
        }

        Populacao novaPopulacao = new Populacao(numeroDeVariaveis, mu, interMin, interMax);
        randomNum = rand.nextInt((2 - 1) + 1) + 1;

        do {
            long t0 = System.currentTimeMillis();

            //Criterio de parada -> Geracoes
            for (int g = 1; g <= geracoes; g++) {
                //Par cada pai gerar lambda/mu filhos

                for (int p = 0; p < populacao.getTamPopulacao(); p++) {
                    for (int f = 0; f < mu / lambda; f++) {
                        //Mutacao
                        Random rnd = new Random();
                        double valor = rnd.nextDouble();
                        if (valor <= this.taxMutacao) {
                            switch (randomNum) {
                                case 1:
                                    try {
                                        Individuo filho = (Individuo) populacao.getIndividuos().get(p).clone();
                                        mutacaoPorBit(filho);
                                        //Avaliar Filho
                                        filho = problema.calcularFuncaoObjetivo(filho);
                                        //Adiciona nova populacao
                                        novaPopulacao.getIndividuos().add(filho);
                                        //Buscal local
                                    } catch (CloneNotSupportedException ex) {
                                        System.out.println("CLONE NÃO FUNCIONOU!!!");
                                    }
                                    break;
                                case 2:
                                    try {
                                        Individuo filho = (Individuo) populacao.getIndividuos().get(p).clone();
                                        mutacaoParcial(filho);
                                        //Avaliar Filho
                                        filho = problema.calcularFuncaoObjetivo(filho);
                                        //Adiciona nova populacao
                                        novaPopulacao.getIndividuos().add(filho);
                                        //Buscal local
                                    } catch (CloneNotSupportedException ex) {
                                        System.out.println("CLONE NÃO FUNCIONOU!!!");
                                    }
                                    break;
                                default:
                                    System.out.println("Número Case nao incluso!");
                                    break;
                            }

                        }

                    }
                }
                populacao.getIndividuos().addAll(novaPopulacao.getIndividuos());
                novaPopulacao.getIndividuos().clear();
                Collections.sort(populacao.getIndividuos());
                populacao.getIndividuos().subList(this.mu, populacao.getIndividuos().size()).clear();

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
//            System.err.println("Isso:" + execucao + ",geracoes," + g + "," + populacao.getMelhorIndividuo().getFuncaoObjetivo());
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

    private void mutacaoPorBit(Individuo filho) {
        Random rnd = new Random();

        double valor;
        //Tenta realizar a mutação sob cada cromossomo do individuo
        for (int i = 0; i < filho.getCromossomos().size(); i++) {
            //gera um numero aleatorio entre 0 e 1 -> somente realiza a mutacao se o valor gerado for menos que a taxa de mutacao
            if (rnd.nextDouble() <= this.taxMutacao) {
                do {
                    valor = this.getInterMin() + (this.getInterMax() - this.getInterMin()) * rnd.nextDouble();
                    filho.getCromossomos().set(i, valor);
                    if (valor < this.getInterMin() || valor > this.getInterMax()) {
                        valor = -this.getInterMin() + (this.getInterMax() - this.getInterMin()) * rnd.nextDouble();
                        filho.getCromossomos().set(i, valor);
                    }
                } while (valor < this.getInterMin() || valor > this.getInterMax());
            }
        }
    }

    private void mutacaoParcial(Individuo filho) {
        Random rnd = new Random();

        double valor;
        //Realiza mutação sobre a primeira metate do individuo

        for (int i = 0; i < filho.getCromossomos().size() / 2; i++) {
            do {
                valor = this.getInterMin() + (this.getInterMax() - this.getInterMin()) * rnd.nextDouble();
                filho.getCromossomos().set(i, valor);
                if (valor < this.getInterMin() || valor > this.getInterMax()) {
                    valor = -this.getInterMin() + (this.getInterMax() - this.getInterMin()) * rnd.nextDouble();
                    filho.getCromossomos().set(i, valor);
                }
            } while (valor < this.getInterMin() || valor > this.getInterMax());
        }
    }

    public int getGeracoes() {
        return geracoes;
    }

    public void setGeracoes(int geracoes) {
        this.geracoes = geracoes;
    }

    public int getNumeroDeVariaveis() {
        return numeroDeVariaveis;
    }

    public void setNumeroDeVariaveis(int numeroDeVariaveis) {
        this.numeroDeVariaveis = numeroDeVariaveis;
    }

    public int getMu() {
        return mu;
    }

    public void setMu(int mu) {
        this.mu = mu;
    }

    public int getLambda() {
        return lambda;
    }

    public void setLambda(int lambda) {
        this.lambda = lambda;
    }

    public double getTaxMutacao() {
        return taxMutacao;
    }

    public void setTaxMutacao(double taxMutacao) {
        this.taxMutacao = taxMutacao;
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

    public Problema getProblema() {
        return problema;
    }

    public void setProblema(ProblemaRastringin problema) {
        this.problema = problema;
    }

}
