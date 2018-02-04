/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package provace;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import metodo.EvolucaoDiferencial;
import problema.ProblemaSchwefelsFunction;
import solucao.Individuo;

/**
 *
 * @author aline
 */
public class ProvaCE {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        int geracoes;
        int execucoes = 30;
        int variaveis;
        int tamPopulacao;
        double txReplace;

        double F;
        double Cr;
        double min = -500;
        double max = 500;
        //lahc
        int limite = 15, cont = 0, timeoutlahc = 100, execucao = 0;
        EvolucaoDiferencial ed;
        problema.ProblemaSchwefelsFunction problema = new ProblemaSchwefelsFunction();
        Individuo melhor = null, pior = null;
        int vetor[] = new int[]{1, 2, 3, 4};
        while (execucao < execucoes) {
            Arrays.sort(vetor);
            for (int i = 0; i < vetor.length; i++) {
                switch (vetor[i]) {

                    case 1:
                        geracoes = 300;
                        variaveis = 40;
                        tamPopulacao = 100;
                        txReplace = 0.5;
                        F = 0.8;
                        Cr= 0.4;

                        ed = new EvolucaoDiferencial(min, max, geracoes, variaveis, tamPopulacao, F, Cr, problema, txReplace);
                        salvarDados(ed, execucao, 1);
                        cont++;
                        break;
                    case 2:
                        geracoes = 500;
                        variaveis = 50;
                        tamPopulacao = 80;
                        txReplace = 0.3;
                        F = 0.8;
                        Cr= 0.3;
                        
                        ed = new EvolucaoDiferencial(min, max, geracoes, variaveis, tamPopulacao, F, Cr, problema, txReplace);
                        salvarDados(ed, execucao, 2);
                        cont++;
                        break;
                    case 3:
                        geracoes = 100;
                        variaveis = 80;
                        tamPopulacao = 200;
                        txReplace = 0.6;
                        F = 0.9;
                        Cr= 0.5;

                        ed = new EvolucaoDiferencial(min, max, geracoes, variaveis, tamPopulacao, F, Cr, problema, txReplace);
                        salvarDados(ed, execucao, 3);
                        cont++;
                        break;
                    case 4:
                        geracoes = 800;
                        variaveis = 10;
                        tamPopulacao = 50;
                        txReplace = 0.5;
                        F = 0.5;
                        Cr= 0.5;

                        ed = new EvolucaoDiferencial(min, max, geracoes, variaveis, tamPopulacao, F, Cr, problema, txReplace);
                        salvarDados(ed, execucao, 4);
                        cont++;
                        break;
                }
            }

            execucao++;
        }//fecha while

    }

    public static void salvarDados(EvolucaoDiferencial ed, int execucao, int caso) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/home/aline/Documents/Documents/Ufop/8 periodo/Computação Evolucionária/Prova/Prova.csv", true)));
        //PROCESSA O AG
        out.print(ed.executar(execucao, caso));
//            System.out.println(ed.executar());
        out.close();
    }
}
