/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agtsp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author daniel
 */
public class Problema {

    private String nomeArquivo;
    private String nomeInstancia;
    private String funcaoCalculo;
    int dimensao;

    private Double[][] coordenadas;
    private Double[][] distancias;

    public Problema(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;

        readFile(this.nomeArquivo);
        calcularDistancias();
    }

    public void readFile(String namefile) {
        // Recuperando os dados e importando
        try {
            BufferedReader br = new BufferedReader(new FileReader(namefile));

            String linha;
            String dados[];

            // Nome da instância
            linha = br.readLine();
            dados = linha.split(":");
            this.nomeArquivo = dados[1];

            // Comentário
            br.readLine();

            // Tipo
            br.readLine();

            // Dimensão
            linha = br.readLine();
            dados = linha.split(":");
            this.dimensao = Integer.parseInt(dados[1].trim());

            // Instânciação
            this.coordenadas = new Double[this.dimensao][2];
            this.distancias = new Double[this.dimensao][this.dimensao];

            //Função de Calculo(ATTP, EUC_2d)
            linha = br.readLine();
            
            dados = linha.split(":");
            funcaoCalculo = dados[1].trim();
            // Cabeçalho
            br.readLine();

            // Percorre linha a linha
            while ((linha = br.readLine()) != null) {

                if (linha.equals("EOF")) {
                    break;
                }

                dados = linha.split(" ");

                // Identificador
                int id = Integer.parseInt(dados[0]);
                // X
                this.coordenadas[id - 1][0] = Double.parseDouble(dados[1]);
                // Y
                this.coordenadas[id - 1][1] = Double.parseDouble(dados[2]);

                System.out.println(linha);
            }

            br.close();

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException - " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException - " + e.getMessage());
        }
    }

    public void calcularDistancias(){
    
        switch(funcaoCalculo){
            case "ATT":
                this.calcularDistanciasATT();
                break;
            case "EUC_2D":
                this.calcularDistanciasEUC2D();
                break;
            default:
                throw new UnsupportedOperationException("Função não implementada!"+funcaoCalculo);
        }
    
    }
    //Adaptador de : https://github.com/MOEFramework//MOEAFramework
    private void calcularDistanciasATT(){
        Double dist;
        for (int i = 0; i < this.dimensao; i++) {
            for (int j = 0; j < this.dimensao; j++) {
                if (i == j) {
                    dist = 0.0;
                } else {
                    Double x1 = coordenadas[i][0];
                    Double x2 = coordenadas[j][0];
                    Double y1 = coordenadas[i][1];
                    Double y2 = coordenadas[j][1];

                    dist = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                    dist = dist/10.0;
                    double t = Math.round(dist);
                    
                    if(t < dist){
                        dist = t + 1.0;
                    }else{
                        dist = t;
                    }
                }

                this.distancias[i][j] = dist;
            }
        }
    }
    
    private void calcularDistanciasEUC2D() {
        // EUC_2D
        Double dist;
        for (int i = 0; i < this.dimensao; i++) {
            for (int j = 0; j < this.dimensao; j++) {
                if (i == j) {
                    dist = 0.0;
                } else {
                    Double x1 = coordenadas[i][0];
                    Double x2 = coordenadas[j][0];
                    Double y1 = coordenadas[i][1];
                    Double y2 = coordenadas[j][1];

                    dist = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                }

                this.distancias[i][j] = dist;
            }
        }

        // Impressão
        System.out.println("\n\nMatriz de distâncias\n");
        for (int i = 0; i < this.dimensao; i++) {
            for (int j = 0; j < this.dimensao; j++) {
                String value = String.valueOf(this.distancias[i][j]);

                int pos = 0;
                while (value.charAt(pos) != '.' && pos < value.length() - 1) {
                    pos++;
                }
                if (pos > 0 && pos + 2 < value.length() - 1) {
                    value = value.substring(0, pos + 3);
                }

                System.out.print(value + "\t");
            }
            System.out.println("");
        }
    }

    public void calcularFuncaoObjetivo(Individuo individuo) {
        Double custo = 0.0;
        for (int i = 0; i < this.dimensao - 1; i++) {
//            System.out.println("D:"+this.dimensao+" Ind:"+individuo.getCromossomos().size());
            custo += this.distancias[individuo.getCromossomos().get(i) - 1][individuo.getCromossomos().get(i + 1) - 1];
        }
        individuo.setFuncaoObjetivo(custo);
    }

    @Override
    public String toString() {
        return "Problema{" + "nomeArquivo=" + nomeArquivo + ", nomeInstancia=" + nomeInstancia + ", funcaoCalculo=" + funcaoCalculo + ", dimensao=" + dimensao + ", coordenadas=" + coordenadas + ", distancias=" + distancias + '}';
    }

    

}
