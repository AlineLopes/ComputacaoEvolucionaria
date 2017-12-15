/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atividade1;

import java.util.Collections;
import java.util.Random;

/**
 *
 * @author aline
 */
public class AlgoritmoGenetico {

    int geracoes; //CRITÉRIO DE PARADA
    int numeroDeVariaveis; //VARIAVEIS
    int tamPopulacao; // VARIAVEIS
    double taxMutacao, taxCrossover; //TAXAS
    double interMin, interMax; //INTERVALO
    Individuo melhorSolucao, piorSolucao;

    public AlgoritmoGenetico(int geracoes, int numeroDeVariaveis, int tamPopulacao, double taxMutacao, double taxCrossover, double interMin, double interMax) {
        this.geracoes = geracoes;
        this.numeroDeVariaveis = numeroDeVariaveis;
        this.tamPopulacao = tamPopulacao;
        this.taxMutacao = taxMutacao;
        this.taxCrossover = taxCrossover;
        this.interMin = interMin;
        this.interMax = interMax;
    }

    public String executar(int execucao) throws CloneNotSupportedException {
        String retorno = "";
        //CASO DE TESTE
        Random rand = new Random();
        int randomNum = 0, cont = 0; // DEFINIR CASE
        randomNum = rand.nextInt((2 - 1) + 1) + 1;

        do {
            long t0 = System.currentTimeMillis();

            Populacao populacao = new Populacao(numeroDeVariaveis, tamPopulacao, interMin, interMax);
            Populacao novaPopulacao = new Populacao(numeroDeVariaveis, tamPopulacao, interMin, interMax);

            //CRIAR POPULACAO INICIAL
            populacao.criarPopulacao();
            //AVALIAR POPULACAO INICIAL
            populacao.avaliar();
            //RECUPERAR MELHOR INDIVIDUO
            melhorSolucao = (Individuo) populacao.getMelhorIndividuo().clone();
            piorSolucao = (Individuo) populacao.getPiorIndividuo().clone();

//        System.out.println("Solução Inicial: " + melhorSolucao.getFuncaoObjetivo());
            int pai1, pai2;

            //GERACOES
            for (int ger = 1; ger < geracoes; ger++) {
                //PERCORRER A QUANTIDADE DE INDIVIDUOS
                //PARA A REPRODUCAO
                novaPopulacao.getIndividuos().clear();
                for (int i = 0; i < this.tamPopulacao; ++i) {
                    //REPRODUCAO
                    //AVALIAR A APLICACAO DO OPERADOR
                    Random rnd = new Random();
                    double valorRnd = rnd.nextDouble();

                    if (valorRnd <= this.taxCrossover) {
                        //SELECIONAR PAIS-TORNEIO
                        int num1, num2;
                        num1 = rnd.nextInt(this.tamPopulacao);
                        num2 = rnd.nextInt(this.tamPopulacao);
                        if (populacao.getIndividuos().get(num1).getFuncaoObjetivo() > populacao.getIndividuos().get(num2).getFuncaoObjetivo()) {
                            pai1 = num1;
                        } else {
                            pai1 = num2;
                        }
                        do {
                            num1 = rnd.nextInt(this.tamPopulacao);
                            num2 = rnd.nextInt(this.tamPopulacao);
                            if (populacao.getIndividuos().get(num1).getFuncaoObjetivo() > populacao.getIndividuos().get(num2).getFuncaoObjetivo()) {
                                pai2 = num1;
                            } else {
                                pai2 = num2;
                            }
                        } while (pai1 == pai2);

                        Individuo individuo1 = null, individuo2 = null;

                        switch (randomNum) {
                            case 1:
                                //COPIAR OS PAIS NOS DESCEDENTES
                                individuo1 = (Individuo) populacao.getIndividuos().get(pai1).clone();
                                individuo2 = (Individuo) populacao.getIndividuos().get(pai2).clone();

                                //OPERADOR - CROSSOVER
                                this.crossover(populacao.getIndividuos().get(pai1), populacao.getIndividuos().get(pai2), individuo1);
                                this.crossover(populacao.getIndividuos().get(pai2), populacao.getIndividuos().get(pai1), individuo2);

                                //OPERADOR - MUTACAO
                                mutacao1(individuo1);
                                mutacao1(individuo2);
                                break;
                            case 2:
                                //COPIAR OS PAIS NOS DESCEDENTES
                                individuo1 = (Individuo) populacao.getIndividuos().get(pai1).clone();
                                individuo2 = (Individuo) populacao.getIndividuos().get(pai2).clone();

                                //OPERADOR - CROSSOVER
                                this.crossover(populacao.getIndividuos().get(pai1), populacao.getIndividuos().get(pai2), individuo1);
                                this.crossover(populacao.getIndividuos().get(pai2), populacao.getIndividuos().get(pai1), individuo2);

                                //OPERADOR - MUTACAO
                                mutacao2(individuo1);
                                mutacao2(individuo2);
                                break;
                            default:
                                System.out.println("Random deu fora do intervalo!");
                                break;
                        }
//                    //COPIAR OS PAIS NOS DESCEDENTES
//                    individuo1 = (Individuo) populacao.getIndividuos().get(pai1).clone();
//                    individuo2 = (Individuo) populacao.getIndividuos().get(pai2).clone();
//
//                    //OPERADOR - CROSSOVER
//                    this.crossover(populacao.getIndividuos().get(pai1), populacao.getIndividuos().get(pai2), individuo1);
//                    this.crossover(populacao.getIndividuos().get(pai2), populacao.getIndividuos().get(pai1), individuo2);
//
//                    //OPERADOR - MUTACAO
//                    mutacao(individuo1);
//                    mutacao(individuo2);

                        //AVALIAR DESCENDENTES
                        Problema problema = new Problema();
                        problema.calcularFuncaoObjetivo(individuo1);
                        problema.calcularFuncaoObjetivo(individuo2);

                        //ADICIONAR NA NOVA POPULACAO
                        novaPopulacao.getIndividuos().add(individuo1);
                        novaPopulacao.getIndividuos().add(individuo2);
                    }
                }
                //DEFINIR SOBREVIVENCIA -> POPULACAO + DESCENDENTES
                //COMBINAR POPULACOES
                populacao.getIndividuos().addAll(novaPopulacao.getIndividuos());
                //ORDENA INDIVIDUOS
                Collections.sort(populacao.getIndividuos());
                //SELECIONAR MELHORES INDIVIDUOS COM CORTE
                populacao.getIndividuos().subList(tamPopulacao, populacao.getIndividuos().size()).clear();
                if (populacao.getMelhorIndividuo().getFuncaoObjetivo() < this.getMelhorSolucao().getFuncaoObjetivo()) {
                    this.setMelhorSolucao((Individuo) populacao.getMelhorIndividuo().clone());
                }
                if (populacao.getPiorIndividuo().getFuncaoObjetivo() > this.getPiorSolucao().getFuncaoObjetivo()) {
                    this.setPiorSolucao((Individuo) populacao.getPiorIndividuo().clone());
                }
//            System.out.println("Gerações = " + ger + "\tFO - Melhor Solucao = " + this.getMelhorSolucao().getFuncaoObjetivo() + "\tFO- Pior Solucao = " + this.getPiorSolucao().getFuncaoObjetivo() + "\tPopulacao = " + populacao.getIndividuos().size());
            }
            retorno += execucao + "," + randomNum + "," + this.getMelhorSolucao().getFuncaoObjetivo() + "," + (System.currentTimeMillis() - t0) + "\n";
            cont++;
            if (randomNum == 1) {
                randomNum = 2;
            } else {
                randomNum = 1;
            }
        } while (cont < 2);
        return retorno;
    }

    private void crossover(Individuo pai1, Individuo pai2, Individuo filho) {
        Random rnd = new Random();
        int corte;
        corte = rnd.nextInt(pai1.getCromossomos().size());

        filho.getCromossomos().clear();
        filho.getCromossomos().addAll(pai1.getCromossomos().subList(0, corte));
        filho.getCromossomos().addAll(pai2.getCromossomos().subList(corte, pai2.getCromossomos().size()));

    }

    private void mutacao1(Individuo filho) {

        Random rnd = new Random();
        double valor;
        for (int i = 0; i < filho.getCromossomos().size(); i++) {
            double valorRnd = rnd.nextDouble();
            if (valorRnd <= this.taxMutacao) {
                valor = this.getInterMin() + (this.getInterMax() - this.getInterMin()) * rnd.nextDouble();
                if (valor >= this.getInterMin() && valor <= this.getInterMax()) {
                    filho.getCromossomos().set(i, valor);
                }
            }
        }
    }

    private void mutacao2(Individuo filho) {

        Random rnd = new Random();
        double valor;
        //REALIZA MUTAÇÃO
        double valorRnd = rnd.nextDouble();
        if (valorRnd <= this.taxMutacao) {

            //CORTE NA MUTaCAO
            int gene1, gene2;
            Random rndGene = new Random();
            gene1 = rndGene.nextInt(filho.getCromossomos().size());
            do {
                gene2 = rndGene.nextInt(filho.getCromossomos().size());
            } while (gene2 == gene1);

            int cont = 0;
            while (cont < 2) {
                valor = this.getInterMin() + (this.getInterMax() - this.getInterMin()) * rnd.nextDouble();
                if (valor >= this.getInterMin() && valor <= this.getInterMax()) {
                    filho.getCromossomos().set(gene1, valor);
                }
                cont++;
            }
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

    public int getTamPopulacao() {
        return tamPopulacao;
    }

    public void setTamPopulacao(int tamPopulacao) {
        this.tamPopulacao = tamPopulacao;
    }

    public double getTaxMutacao() {
        return taxMutacao;
    }

    public void setTaxMutacao(int taxMutacao) {
        this.taxMutacao = taxMutacao;
    }

    public double getTaxCrossover() {
        return taxCrossover;
    }

    public void setTaxCrossover(int taxCrossover) {
        this.taxCrossover = taxCrossover;
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

}
