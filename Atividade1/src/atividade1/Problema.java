/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atividade1;

/**
 *
 * @author aline
 */
public class Problema {
    
    public void calcularFuncaoObjetivo(Individuo individuo){
        
        Double resultado = 0.0;
        resultado = 10.0 * individuo.getNumeroDeVariaveis()+somatorio(individuo);
        
        individuo.setFuncaoObjetivo(resultado);
    }
    public Double somatorio(Individuo individuo){
        Double somatorio = 0.0;
        for(int i = 0; i < individuo.getNumeroDeVariaveis(); i++){
         somatorio=+ individuo.getCromossomos().get(i) * individuo.getCromossomos().get(i) - 10.0 * Math.cos(2 * Math.PI * individuo.getCromossomos().get(i));
        }
        return somatorio;
    }
}
