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
public interface Problema {
    Individuo calcularFuncaoObjetivo(Individuo individuo);
    int getDimensao();
}
