/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codificacacioninformacion;
import java.util.Comparator;

public class ComparadorElementos implements Comparator<Elemento> {    

    @Override
    public int compare(Elemento o1, Elemento o2) {
        return o2.obtenerFrecuecia() - o1.obtenerFrecuecia();
    }
}
