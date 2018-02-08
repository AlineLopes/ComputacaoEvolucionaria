/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agtsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aline
 */
public class AlgoritmoGenetico {

    int tamPop;
    double pCrossover;
    double pMutacao;
    double pBuscaLocal;
    int geracoes;
    double txReplace;

    Individuo melhorSolucao;

    Populacao populacao;
    Populacao novaPopulacao;

    Problema problema;

    public AlgoritmoGenetico(int tamPop, double pCrossover, double pMutacao, double pBuscaLocal, int geracoes, double txReplace, Problema problema) {
        this.tamPop = tamPop;
        this.pCrossover = pCrossover;
        this.pMutacao = pMutacao;
        this.pBuscaLocal = pBuscaLocal;
        this.geracoes = geracoes;
        this.problema = problema;
        this.txReplace = txReplace;
    }

    public int getTamPop() {
        return tamPop;
    }

    public void setTamPop(int tamPop) {
        this.tamPop = tamPop;
    }

    public double getpCrossover() {
        return pCrossover;
    }

    public void setpCrossover(double pCrossover) {
        this.pCrossover = pCrossover;
    }

    public double getpMutacao() {
        return pMutacao;
    }

    public void setpMutacao(double pMutacao) {
        this.pMutacao = pMutacao;
    }

    public int getGeracoes() {
        return geracoes;
    }

    public void setGeracoes(int geracoes) {
        this.geracoes = geracoes;
    }

    public Individuo getMelhorSolucao() {
        return melhorSolucao;
    }

    public void setMelhorSolucao(Individuo melhorSolucao) {
        this.melhorSolucao = melhorSolucao;
    }

    public Populacao getPopulacao() {
        return populacao;
    }

    public void setPopulacao(Populacao populacao) {
        this.populacao = populacao;
    }

    public Populacao getNovaPopulacao() {
        return novaPopulacao;
    }

    public void setNovaPopulacao(Populacao novaPopulacao) {
        this.novaPopulacao = novaPopulacao;
    }

    public Problema getProblema() {
        return problema;
    }

    public void setProblema(Problema problema) {
        this.problema = problema;
    }

    public double getTxReplace() {
        return txReplace;
    }

    public void setTxReplace(double txReplace) {
        this.txReplace = txReplace;
    }

    public String executar(int execucao) throws CloneNotSupportedException {
        long t0 = System.currentTimeMillis();

        populacao = new Populacao(tamPop, problema);
        novaPopulacao = new Populacao(tamPop, problema);

        // Geração da população inicial
        populacao.criar();

        // Avaliar a população inicial
        populacao.avaliar();

        try {
            // Recuperar o melhor indivíduo
            melhorSolucao = (Individuo) populacao.getMelhorIndividuo().clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(AlgoritmoGenetico.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Informação sobre a solução inicial 
        System.out.println("Solução inicial: " + melhorSolucao.getFuncaoObjetivo());

        Random rnd = new Random();
        int pai1 = 0, pai2 = 0;

        // Número de gerações - critério de parada
        for (int gen = 1; gen < geracoes; gen++) {

            // Reprodução
            novaPopulacao.getIndividuos().clear();

            // Percorrer a populacao corrente
            // E selecionar os pais
            for (int j = 0; j < tamPop; j++) {

                // Se o crossover deve ser aplicado
                if (rnd.nextDouble() <= pCrossover) {
                    // -- Selecionar os pais
                    pai1 = rnd.nextInt(tamPop);
                    do {
                        pai2 = rnd.nextInt(tamPop);
                    } while (pai1 == pai2);

                    Individuo filho1 = new Individuo(problema.dimensao);
                    Individuo filho2 = new Individuo(problema.dimensao);

                    crossoverOX(this.populacao.getIndividuos().get(pai1), this.populacao.getIndividuos().get(pai2), filho1, filho2);

//                    System.out.println(this.populacao.getIndividuos().get(pai1));
//                    System.out.println(this.populacao.getIndividuos().get(pai2));
//                    System.out.println(filho1);
//                    System.out.println(filho2);
                    mutacaoSWAP(filho1);
                    mutacaoSWAP(filho2);

//                    System.out.println(filho1);
//                    System.out.println(filho2);
//                    
//                    System.exit(0);
                    //submeter ao replace
                    replace(filho1);
                    replace(filho2);

                    // Avaliar descendentes
                    problema.calcularFuncaoObjetivo(filho1);
                    problema.calcularFuncaoObjetivo(filho2);

                    // Busca local
                    if (rnd.nextDouble() <= this.pBuscaLocal) {
                        buscaLocal(filho1);
                    }
                    if (rnd.nextDouble() <= this.pBuscaLocal) {
                        buscaLocal(filho2);
                    }

                    // Inserir na nova populacao
                    novaPopulacao.getIndividuos().add(filho1);
                    novaPopulacao.getIndividuos().add(filho2);

                }

            }

            // Definir sobreviventes - pop corrente + descendentes
            // Combinar POP+NovaPOP
            populacao.getIndividuos().addAll(novaPopulacao.getIndividuos());
            // Ordenar individuos
            Collections.sort(populacao.getIndividuos());
            // Cortar no tamanho da populacao
            populacao.getIndividuos()
                    .subList(tamPop, populacao.getIndividuos().size()).clear();

            // Melhor solucao corrente
            if (populacao.getMelhorIndividuo()
                    .getFuncaoObjetivo()
                    < this.getMelhorSolucao()
                            .getFuncaoObjetivo()) {
                try {
                    this.setMelhorSolucao((Individuo) populacao.getMelhorIndividuo()
                            .clone());
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(AlgoritmoGenetico.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

//            System.out.println("Gen = " + gen
//                    + "\tFO = " + this.getMelhorSolucao().getFuncaoObjetivo()
//                    + "\tPop = " + populacao.getIndividuos().size());
        }
        String retorno = execucao+","+this.txReplace+"," + this.getMelhorSolucao().getFuncaoObjetivo() + "," + (System.currentTimeMillis() - t0);
        return retorno;

    }

    private void crossoverOX(Individuo pai1, Individuo pai2, Individuo filho1, Individuo filho2) {

        Random rnd = new Random();

        int i, j;

        i = rnd.nextInt(problema.dimensao / 2);
        do {
            j = rnd.nextInt(problema.dimensao);
        } while (i == j || i > j);

//        if (i > j) {
//            int aux = i;
//            i = j;
//            j = aux;
//        }
        // Parte central entre I e J
        filho1.getCromossomos().addAll(pai1.getCromossomos().subList(i, j));
        filho2.getCromossomos().addAll(pai2.getCromossomos().subList(i, j));

        // Copiar de P2 para F1
        int idx = j;
        int k = j;

        while (k < problema.dimensao) {
            if (!filho1.getCromossomos().contains(pai2.getCromossomos().get(k))) {
                filho1.getCromossomos().add(pai2.getCromossomos().get(k));
                idx++;

                if (idx == problema.dimensao) {
                    break;
                }

            }

            k++;
            if (k == problema.dimensao) {
                k = 0;
            }

        }

        idx = 0;
        k = 0;

        while (k < problema.dimensao) {
            if (!filho1.getCromossomos().contains(pai2.getCromossomos().get(k))) {
                filho1.getCromossomos().add(idx, pai2.getCromossomos().get(k));
                idx++;

                if (idx == problema.dimensao || filho1.getCromossomos().size() == problema.dimensao) {
                    break;
                }

            }

            k++;

        }

        // Copiar de P1 para F2         
        idx = j;
        k = j;

        while (k < problema.dimensao) {
            if (!filho2.getCromossomos().contains(pai1.getCromossomos().get(k))) {
                filho2.getCromossomos().add(pai1.getCromossomos().get(k));
                idx++;

                if (idx == problema.dimensao) {
                    break;
                }

            }

            k++;
            if (k == problema.dimensao) {
                k = 0;
            }

        }

        idx = 0;
        k = 0;

        while (k < problema.dimensao) {
            if (!filho2.getCromossomos().contains(pai1.getCromossomos().get(k))) {
                filho2.getCromossomos().add(idx, pai1.getCromossomos().get(k));
                idx++;

                if (idx == problema.dimensao || filho2.getCromossomos().size() == problema.dimensao) {
                    break;
                }

            }

            k++;

        }

    }

    public void replace(Individuo individuo) throws CloneNotSupportedException {

        Random rnd = new Random();

        for (int i = 1; i < individuo.getCromossomos().size() - 1; i++) {
            if (rnd.nextDouble() <= this.txReplace) {

                for (int j = 0; j < melhorSolucao.getCromossomos().size(); j++) {
                    if (Objects.equals(melhorSolucao.getCromossomos().get(j), individuo.getCromossomos().get(i))) {
                        int aux = individuo.getCromossomos().get(j);
                        individuo.getCromossomos().set(j, individuo.getCromossomos().get(i));
                        individuo.getCromossomos().set(i, aux);
                    }
                }
            }
        }
    }

    private void mutacaoSWAP(Individuo individuo) {

        Random rnd = new Random();

        // Verifica o processo de mutacao para cada gene do cromossomo
        for (int i = 0; i < individuo.getCromossomos().size(); i++) {
            if (rnd.nextDouble() <= pMutacao) {

                int j;
                do {
                    j = rnd.nextInt(problema.dimensao);
                } while (i == j);

                Collections.swap(
                        individuo.getCromossomos(), i, j);
            }
        }

    }
    //1) remover u e inserir após v;
    //2) remover u e x e inserir u e x após v;
    //3) remover u e x e inserir x e u após v; (posições invertidas)
    //4) trocar u e v;
    //5) troca u e x com v;
    //6) troca u e x com v e y;

    public void buscaLocal(Individuo individuo) {

        ArrayList<Integer> operacoes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        Collections.shuffle(operacoes);

        for (Integer i : operacoes) {
            switch (i) {
                case 1:
                    buscaLocal1(individuo);
                    break;
                case 2:
                    buscaLocal2(individuo);
                    break;
                case 3:
                    buscaLocal3(individuo);
                    break;
                case 4:
                    buscaLocal4(individuo);
                    break;
                case 5:
                    buscaLocal5(individuo);
                    break;
                case 6:
                    buscaLocal6(individuo);
                    break;
            }
        }

    }
    // Busca local - 
    //1) remover u e inserir após v;

    public void buscaLocal1(Individuo individuo) {

        double foInicial = individuo.getFuncaoObjetivo();

        for (int u = 0; u < individuo.getCromossomos().size() - 1; u++) {
            for (int v = u + 1; v < individuo.getCromossomos().size(); v++) {

                int valorU = individuo.getCromossomos().get(u);

                individuo.getCromossomos().remove(u);
                individuo.getCromossomos().add(v, valorU);

                // Calcular FO - Delta
                problema.calcularFuncaoObjetivo(individuo);

                // Se existe melhora
                if (individuo.getFuncaoObjetivo() < foInicial) {
                    // Encerra - first improvement
                    return;
                } else {
                    // Desfazer a troca
                    individuo.getCromossomos().remove(v);
                    individuo.getCromossomos().add(u, valorU);
                }

            }
        }

        // Retorna valor original da FO
        // Nao aconteceu nenhuma mudanca
        individuo.setFuncaoObjetivo(foInicial);

    }

    // Busca local - 
    //2) remover u e x e inserir u e x após v;
    public void buscaLocal2(Individuo individuo) {

        double foInicial = individuo.getFuncaoObjetivo();

        for (int u = 0; u < individuo.getCromossomos().size() - 2; u++) {
            int x = u + 1;
            for (int v = x + 1; v < individuo.getCromossomos().size(); v++) {

                int valorU = individuo.getCromossomos().get(u);
                int valorX = individuo.getCromossomos().get(x);

                int valorV = individuo.getCromossomos().get(v);

                individuo.getCromossomos().remove(u);
                individuo.getCromossomos().remove(x - 1);

                // Nova posicao de V
                int posV = individuo.getCromossomos().indexOf(valorV);
                individuo.getCromossomos().add(posV + 1, valorU);
                individuo.getCromossomos().add(posV + 2, valorX);

                // Calcular FO - Delta
                problema.calcularFuncaoObjetivo(individuo);

                // Se existe melhora
                if (individuo.getFuncaoObjetivo() < foInicial) {
                    // Encerra - first improvement
                    return;
                } else {
                    // Desfazer a troca
                    // Remove U e X
                    int pos = individuo.getCromossomos().indexOf(valorU);
                    individuo.getCromossomos().remove(pos);
                    pos = individuo.getCromossomos().indexOf(valorX);
                    individuo.getCromossomos().remove(pos);

                    // Inserir U e X nas posicoes originais
                    individuo.getCromossomos().add(u, valorU);
                    individuo.getCromossomos().add(u + 1, valorX);
                }

            }
        }

        // Retorna valor original da FO
        // Nao aconteceu nenhuma mudanca
        individuo.setFuncaoObjetivo(foInicial);

    }

    //3) remover u e x e inserir x e u após v;
    public void buscaLocal3(Individuo individuo) {

        double foInicial = individuo.getFuncaoObjetivo();

        for (int u = 0; u < individuo.getCromossomos().size() - 2; u++) {
            int x = u + 1;
            for (int v = x + 1; v < individuo.getCromossomos().size(); v++) {

                int valorU = individuo.getCromossomos().get(u);
                int valorX = individuo.getCromossomos().get(x);

                int valorV = individuo.getCromossomos().get(v);

                individuo.getCromossomos().remove(u);
                individuo.getCromossomos().remove(x - 1);

                // Nova posicao de V
                int posV = individuo.getCromossomos().indexOf(valorV);
                individuo.getCromossomos().add(posV + 1, valorX);
                individuo.getCromossomos().add(posV + 2, valorU);

                // Calcular FO - Delta
                problema.calcularFuncaoObjetivo(individuo);

                // Se existe melhora
                if (individuo.getFuncaoObjetivo() < foInicial) {
                    // Encerra - first improvement
                    return;
                } else {
                    // Desfazer a troca
                    // Remove U e X
                    int pos = individuo.getCromossomos().indexOf(valorU);
                    individuo.getCromossomos().remove(pos);
                    pos = individuo.getCromossomos().indexOf(valorX);
                    individuo.getCromossomos().remove(pos);

                    // Inserir U e X nas posicoes originais
                    individuo.getCromossomos().add(u, valorU);
                    individuo.getCromossomos().add(u + 1, valorX);
                }
            }
        }

        // Retorna valor original da FO
        // Nao aconteceu nenhuma mudanca
        individuo.setFuncaoObjetivo(foInicial);

    }

    public void buscaLocal4(Individuo individuo) {

        //4) trocar u e v;
        // SWAP
        // First improvement
        double foInicial = individuo.getFuncaoObjetivo();

        for (int u = 0; u < individuo.getCromossomos().size() - 1; u++) {
            for (int v = u + 1; v < individuo.getCromossomos().size(); v++) {

                // Opera SWAP
                Collections.swap(individuo.getCromossomos(), u, v);
                // Calcular FO - Delta
                problema.calcularFuncaoObjetivo(individuo);

                // Se existe melhora
                if (individuo.getFuncaoObjetivo() < foInicial) {
                    // Encerra - first improvement
                    return;
                } else {
                    // Desfazer a troca
                    Collections.swap(individuo.getCromossomos(), u, v);
                }

            }
        }

        // Retorna valor original da FO
        // Nao aconteceu nenhuma mudanca
        individuo.setFuncaoObjetivo(foInicial);

    }

    //5) troca u e x com v;
    public void buscaLocal5(Individuo individuo) {

        double foInicial = individuo.getFuncaoObjetivo();

        for (int u = 0; u < individuo.getCromossomos().size() - 2; u++) {
            int x = u + 1;
            for (int v = x + 1; v < individuo.getCromossomos().size(); v++) {

                int valorX = individuo.getCromossomos().get(x);

                // Opera SWAP
                Collections.swap(individuo.getCromossomos(), u, v);
                individuo.getCromossomos().remove(x);
                individuo.getCromossomos().add(v, valorX);

                // Calcular FO - Delta
                problema.calcularFuncaoObjetivo(individuo);

                // Se existe melhora
                if (individuo.getFuncaoObjetivo() < foInicial) {
                    // Encerra - first improvement
                    return;
                } else {
                    // Desfazer a troca
                    Collections.swap(individuo.getCromossomos(), u, v);
                    int pos = individuo.getCromossomos().indexOf(valorX);
                    individuo.getCromossomos().remove(pos);
                    individuo.getCromossomos().add(x, valorX);
                }

            }
        }

        // Retorna valor original da FO
        // Nao aconteceu nenhuma mudanca
        individuo.setFuncaoObjetivo(foInicial);

    }

    //6) troca u e x com v e y;
    public void buscaLocal6(Individuo individuo) {

        double foInicial = individuo.getFuncaoObjetivo();

        for (int u = 0; u < individuo.getCromossomos().size() - 2; u++) {
            int x = u + 1;
            for (int v = x + 1; v < individuo.getCromossomos().size() - 1; v++) {

                int y = v + 1;

                // Opera SWAP
                Collections.swap(individuo.getCromossomos(), u, v);
                Collections.swap(individuo.getCromossomos(), x, y);

                // Calcular FO - Delta
                problema.calcularFuncaoObjetivo(individuo);

                // Se existe melhora
                if (individuo.getFuncaoObjetivo() < foInicial) {
                    // Encerra - first improvement
                    return;
                } else {
                    // Desfazer a troca
                    Collections.swap(individuo.getCromossomos(), u, v);
                    Collections.swap(individuo.getCromossomos(), x, y);
                }

            }
        }

        // Retorna valor original da FO
        // Nao aconteceu nenhuma mudanca
        individuo.setFuncaoObjetivo(foInicial);

    }
}
