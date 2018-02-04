/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema;

import solucao.Individuo;

/**
 *
 * @author aline
 */
public class ProblemaRastringin implements Problema{

    private int dimensao;

    public ProblemaRastringin() {
        this.dimensao = dimensao;
    }
        
    @Override
    public Individuo calcularFuncaoObjetivo(Individuo individuo) {
        Double resultado = 0.0;
        resultado = 10.0 * individuo.getNumeroVariaveis() + somatorio(individuo);
        
        individuo.setFuncaoObjetivo(resultado);
        return individuo;
    }

    public Double somatorio(Individuo individuo){
        Double somatorio = 0.0;
        for (int i = 0; i < individuo.getNumeroVariaveis(); i++) {
           somatorio += individuo.getCromossomos().get(i) * individuo.getCromossomos().get(i) - 10.0 * Math.cos(2 * Math.PI * individuo.getCromossomos().get(i));
        }
        return somatorio;
    }

    @Override
    public int getDimensao() {
        return this.dimensao;
    }
}
