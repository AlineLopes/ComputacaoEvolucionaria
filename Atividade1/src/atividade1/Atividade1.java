/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atividade1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author aline
 */
public class Atividade1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CloneNotSupportedException {
        // TODO code application logic here

        int geracoes = 300; //CRITÉRIO DE PARADA
        int numeroDeVariaveis = 100; //VARIAVEIS
        int tamPopulacao = 100; // VARIAVEIS
        double taxMutacao = 0.3, taxCrossover = 0.8; //TAXAS
        double interMin = -5.12, interMax = 5.12; //INTERVALO

        int execucoes = 30, n = 0;
        while (n < execucoes) {

            AlgoritmoGenetico ag = new AlgoritmoGenetico(geracoes, numeroDeVariaveis, tamPopulacao, taxMutacao, taxCrossover, interMin, interMax);

            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("dadosDaExecucao", true)));
                //PROCESSA O AG
                out.print(ag.executar(n));

                Individuo melhor = ag.getMelhorSolucao();
                Individuo pior = ag.getPiorSolucao();

//                System.out.println("Melhor Solucao = " + melhor + "\nPior Solucao = " + pior);

                out.close();
            } catch (IOException e) {
                //oh noes!
            }
            n++;
        }

    }

}
