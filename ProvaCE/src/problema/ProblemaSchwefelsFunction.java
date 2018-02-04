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
public class ProblemaSchwefelsFunction implements Problema{

    
    
    
    @Override
    public Individuo calcularFuncaoObjetivo(Individuo individuo) {

        Double resultado = 0.0;
        for (int i = 0; i < individuo.getNumeroVariaveis(); i++) {
            resultado += - (individuo.getCromossomos().get(i)*(Math.sin(Math.sqrt(Math.abs(individuo.getCromossomos().get(i))))));
        }
        individuo.setFuncaoObjetivo(resultado);
        
        return individuo;
    }

    @Override
    public int getDimensao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
