package codificacacioninformacion;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ArrayList;
/**
 *
 * @author Danilo Pineda 
 * @Ingeniería de Sistemas
 * @Fundación Universitaria del Área Andina 2020
 */
public class Procesamiento {


    public static boolean Codificar(String mensaje){        
        int cuenta = mensaje.length();
        HashMap<Character, Integer> frecuencias = GenerarFrecuencias(mensaje.toLowerCase());
        ComparadorElementos comparador = new ComparadorElementos();        
        ArrayList<Elemento> elementos = CrearElementos(frecuencias);
        elementos.sort(comparador);
        AsignarDivisiones(elementos, cuenta);
        CalcularOtros(elementos,cuenta);
        MostrarResultados(elementos);
        MostrarMensajeCodificado(mensaje.toLowerCase(),elementos);
        return true;
    }
    
    private static HashMap GenerarFrecuencias(String mensaje){
        
        HashMap<Character, Integer> charCountMap = new HashMap<>();  
        char[] strArray = mensaje.toCharArray();          
        for (char c : strArray)
        {
            if(charCountMap.containsKey(c)){                  
                charCountMap.put(c, charCountMap.get(c)+1);
            }
            else{  
                charCountMap.put(c, 1);
            }
        }
        //System.out.println(mensaje+" : "+charCountMap);        
        System.out.println("\n");
        return charCountMap;
    }
    
    private static ArrayList<Elemento> CrearElementos(HashMap<Character, Integer> frecuencias){        
        ArrayList<Elemento> elementos = new ArrayList<>();        
        frecuencias.forEach((k,v) -> {
            Elemento el = new Elemento();
            el.codigo = k;
            el.frecuencia = v;
            el.divisionBit = "";
            el.entopiaMensaje = "";
            elementos.add(el);
        });        
           
        return elementos;
    }
    
    private static void AsignarDivisiones(ArrayList<Elemento> elementos, int longitud){
        AgregarCodigosBinarios(elementos,longitud, true);
        while(true){
            ArrayList<Elemento> elementosIguales = BuscarDivisionesIguales(elementos);
            if(elementosIguales.isEmpty()) break;
            int longitud2 = ObtenerLongitud(elementosIguales);
            AgregarCodigosBinarios(elementosIguales,longitud2, false);
            ActualizarCodigos(elementos, elementosIguales);           
        }
        
        
    }
    
    private static void CalcularOtros(ArrayList<Elemento> elementos, int cta){
        for (Elemento el : elementos) {
            DecimalFormat df = new DecimalFormat("#.##");
            double div = ((double)cta/(double)el.frecuencia);
            double result = Math.log(div) / Math.log(2);
            el.entropia = df.format(result);
            el.entopiaMensaje = df.format(result * cta);
            el.bitsCodgo = el.divisionBit.length();
            el.bitsMensaje = Math.round(el.bitsCodgo * el.frecuencia);
        }
    }
     
    private static void CalcularEntropiasMensj(ArrayList<Elemento> elementos, int cta){
         for (int i = 0; i < elementos.size(); i++) {
            Elemento el = elementos.get(i);
            DecimalFormat df = new DecimalFormat("#.##");
            double div = ((double)cta/(double)el.frecuencia);
            el.entropia = df.format(Math.log(div) / Math.log(2));
        };
    }
    
    private static void ActualizarCodigos(ArrayList<Elemento> elementos, ArrayList<Elemento> elementosIguales){
          elementosIguales.forEach(x ->{
              int eli = elementos.indexOf(x);
              Elemento el = elementos.get(eli);
              el.bitsCodgo = x.bitsCodgo;
          });
    }
    
    private static void AgregarCodigosBinarios(ArrayList<Elemento> elementos, int longitud, boolean inicial){
        int mitad = Math.round(longitud/2);
        int sumasF = 0;
        for (Elemento el : elementos) {
            //if(inicial){
                if(sumasF < (mitad)){                
                    sumasF += el.frecuencia;
                    el.divisionBit += "0";
                }
                else 
                    el.divisionBit += "1";
            //}
            
        }        
    }
    
    private static int ObtenerLongitud(ArrayList<Elemento> elementos){
        int longitud = 0;
        longitud = elementos.stream().map((elemento) -> elemento.frecuencia).reduce(longitud, Integer::sum);
        return longitud;
    }
          
    private static ArrayList<Elemento> BuscarDivisionesIguales(ArrayList<Elemento> elementos){        
        ArrayList<Elemento> dIguales = new ArrayList<>();
        String buscarPor = null;        
        for (int i = 0; i < elementos.size(); i++) {           
            for (int j = 0; j < elementos.size(); j++) {
                Elemento el1 = elementos.get(i);
                Elemento el2 = elementos.get(j);
                if(el1.divisionBit.equals(el2.divisionBit) && el1.codigo != el2.codigo
                   && (el1.divisionBit.equals(buscarPor) || buscarPor == null)){
                    buscarPor = el2.divisionBit;                    
                     if(dIguales.indexOf(el1) < 0 ) dIguales.add(el1);
                     if(dIguales.indexOf(el2) < 0 ) dIguales.add(el2);                    
                }
            }
        }           
        return dIguales;
    }    
    
    private static void MostrarResultados(ArrayList<Elemento> elementos){        
        final Object[][] table = new String[elementos.size()+1][];
            table[0] = new String[] { "", "Frecuencia", "Division", "Entropía", "Entropía Mensaje", "Bits Codigo", "Bits Mensaje" };
            for(int i = 0; i < elementos.size(); i++ ){                
                Elemento el = elementos.get(i);
                table[i+1] = new String[] { Character.toString(el.codigo),
                                          Integer.toString(el.frecuencia),
                                          el.divisionBit,
                                          el.entropia,
                                          el.entopiaMensaje,
                                          Integer.toString(el.bitsCodgo),
                                          Double.toString(el.bitsMensaje)};
            };
            for (final Object[] row : table) {
                System.out.format("%15s%15s%15s%15s%20s%15s%15s\n", row);
            }           
        
    }
    
    private static void MostrarMensajeCodificado(String mensaje, ArrayList<Elemento> elementos){
        System.out.println("\nMensaje codificado: \n");
         for (int i = 0; i < mensaje.length(); i++) {
             int indice = BuscarElemento(mensaje.charAt(i),elementos);
             String ch = elementos.get(indice).divisionBit;
             String resultado = "";
             
            if(i == 0)
                 resultado += ch;
            if(i < mensaje.length() -1 && i > 0)
                resultado += "-"+ch;
                        
            System.out.print(resultado);
         }
    }
    
    private static int BuscarElemento(char ch, ArrayList<Elemento> elementos){
        int index = -1;
        for (int i = 0; i < elementos.size(); i++) {
            Elemento el = elementos.get(i);
            if(ch == el.codigo){
                index = i;
            }                
        }
        return index;
    }
}