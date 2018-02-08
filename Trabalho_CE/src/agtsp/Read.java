package agtsp;

import agtsp.Problema;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author daniel
 */
public class Read {

    public static void main(String args[]) throws CloneNotSupportedException, IOException {
        Problema tsp = new Problema("/home/aline/Documents/Documents/Ufop/8 periodo/heu_e_met_tsp_instances/att48.tsp");
        System.out.println(tsp);
        EvolucaoDiferencial de;

        int geracoes;
        int execucoes = 30;
        int variaveis;
        int tamPopulacao;
        double txReplace;

        double F;
        double Cr;

//        Individuo individuo = new Individuo(tsp.dimensao);
//        individuo.criar();
//        
//        tsp.calcularFuncaoObjetivo(individuo);
//        System.out.println(individuo);
        int execucao = 0;

        int vetor[] = new int[]{1, 2, 3, 4};
        PrintWriter out;
        while (execucao < execucoes) {
            Arrays.sort(vetor);
            for (int i = 0; i < vetor.length; i++) {
                int op = vetor[i];
                switch (op) {

                    case 1:
                        geracoes = 300;
                        variaveis = 40;
                        tamPopulacao = 100;
                        txReplace = 0.5;
                        F = 0.8;
                        Cr = 0.4;

                        de = new EvolucaoDiferencial(geracoes, variaveis, tamPopulacao, F, Cr, tsp, txReplace);
                        try {

                            out = new PrintWriter(new BufferedWriter(new FileWriter("/home/aline/dadosExecucao.txt", true)));
                            out.println(de.executar(execucao, vetor[i]));
                            out.close();

                        } catch (IOException e) {
                            //oh noes!
                        }

                        break;
                    case 2:
                        geracoes = 500;
                        variaveis = 50;
                        tamPopulacao = 80;
                        txReplace = 0.3;
                        F = 0.8;
                        Cr = 0.3;

                        de = new EvolucaoDiferencial(geracoes, variaveis, tamPopulacao, F, Cr, tsp, txReplace);
                        try {
                            out = new PrintWriter(new BufferedWriter(new FileWriter("/home/aline/dadosExecucao.txt", true)));
                            out.println(de.executar(execucao, vetor[i]));
                            out.close();
                        } catch (IOException e) {
                            //oh noes!
                        }
                        break;
                    case 3:
                        geracoes = 100;
                        variaveis = 80;
                        tamPopulacao = 200;
                        txReplace = 0.6;
                        F = 0.9;
                        Cr = 0.5;
                        de = new EvolucaoDiferencial(geracoes, variaveis, tamPopulacao, F, Cr, tsp, txReplace);
                        try {
                            out = new PrintWriter(new BufferedWriter(new FileWriter("/home/aline/dadosExecucao.txt", true)));
                            out.println(de.executar(execucao, vetor[i]));
                            out.close();
                        } catch (IOException e) {
                            //oh noes!
                        }
                        break;
                    case 4:
                        geracoes = 800;
                        variaveis = 10;
                        tamPopulacao = 50;
                        txReplace = 0.5;
                        F = 0.5;
                        Cr = 0.5;

                        de = new EvolucaoDiferencial(geracoes, variaveis, tamPopulacao, F, Cr, tsp, txReplace);
                        try {
                            out = new PrintWriter(new BufferedWriter(new FileWriter("/home/aline/dadosExecucao.txt", true)));
                            out.println(de.executar(execucao, vetor[i]));
                            out.close();
                        } catch (IOException e) {
                            //oh noes!
                        }
                        break;
                }
            }

            execucao++;
        }//fecha while

    }

}
