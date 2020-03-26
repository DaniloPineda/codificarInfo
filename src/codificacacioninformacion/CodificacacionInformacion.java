/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codificacacioninformacion;
import java.util.Scanner;

public class CodificacacionInformacion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

         while(true){
            Scanner lector = new Scanner(System.in);
            Ejecutar(lector);
            System.out.println("\n\n¿Desea codificar otro mensaje? (S/N)");
            String sino = lector.nextLine();
            if(sino.toLowerCase().startsWith("s"))
                Ejecutar(lector);
            else
                break;
        }        
         
    }
    
    public static void Ejecutar(Scanner lector){
        System.out.println("Ingrese el mensaje a codificar: ");
            String mensaje = lector.nextLine();     
        boolean resultado = Procesamiento.Codificar(mensaje);
            if(resultado)
                System.out.println("\n\nEjecución exitosa");
    }
    
      
}
