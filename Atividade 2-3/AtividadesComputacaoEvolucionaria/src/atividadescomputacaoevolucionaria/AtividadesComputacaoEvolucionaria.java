/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atividadescomputacaoevolucionaria;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import metodo.EstrategiaEvolucionaria;
import metodo.EvolucaoDiferencial;
import problema.Problema;
import problema.ProblemaRastringin;
import solucao.Individuo;

/**
 *
 * @author aline
 */
public class AtividadesComputacaoEvolucionaria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int geracoes = 1000; //CRITÉRIO DE PARADA
        int numeroDeVariaveis = 100; //VARIAVEIS
        int mu = 100, lambda = 10; // VARIAVEIS
        double taxMutacao = 1.0, taxCrossover = 0.8;//TAXAS
        double interMin = -5.12, interMax = 5.12; //INTERVALO

//        int geracoes = 1; //CRITÉRIO DE PARADA
//        int numeroDeVariaveis = 10; //VARIAVEIS
//        int mu = 10, lambda = 2; // VARIAVEIS
//        double taxMutacao = 0.8;//TAXAS
//        double interMin = -5.12, interMax = 5.12; //INTERVALO
        int execucoes = 30, n = 0; //execuções = 30
        Problema problema = new ProblemaRastringin();
        int op = 2;
        Individuo melhor = null,
                 pior = null;
        switch (op) {
            case 1:
                
                while (n < execucoes) {
                    EstrategiaEvolucionaria ee = new EstrategiaEvolucionaria(geracoes, numeroDeVariaveis, mu, lambda, taxMutacao, interMin, interMax, problema);
                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("dadosDaExecucaoEE", true)));
                        //PROCESSA O AG
                        out.print(ee.executar(n));

                        melhor = ee.getMelhorSolucao();
                        pior = ee.getPiorSolucao();

//                        System.out.println("Melhor Solucao = " + melhor + "\nPior Solucao = " + pior);
                        out.close();
                    } catch (IOException e) {
                        //oh noes!
                    }

                    n++;
                }
                System.out.println("Melhor Solução: " + melhor.toString());
                break;
            case 2:
                while (n < execucoes) {
                    EvolucaoDiferencial ed = new EvolucaoDiferencial(interMin, interMax, geracoes, numeroDeVariaveis, mu, taxMutacao, taxCrossover, problema);
                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("dadosDaExecucaoED", true)));
                        //PROCESSA O AG
                        out.print(ed.executar(n));

                        melhor = ed.getMelhorSolucao();
                        pior = ed.getPiorSolucao();

//                        System.out.println("Melhor Solucao = " + melhor + "\nPior Solucao = " + pior);
                        out.close();
                    } catch (IOException e) {
                        //oh noes!
                    }

                    n++;
                }
                System.out.println("Melhor Solução: " + melhor.toString());

                break;
            default:
        }

    }

}
