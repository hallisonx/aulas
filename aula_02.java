/******************************************************************************

Aula 2 - Estruturas de Controle

*******************************************************************************/
public class Main
{
	public static void main(String[] args) {
		System.out.println("Exemplo 1");
		boolean x = true;
		boolean y = false;
		
		if(x || y){
		    System.out.println("maçã");
		}else{
		    System.out.println("banana");
		}
		
        
        for(int i=0; i <= 10; i++){
            System.out.printf("valor de i: %s \n", i);
        }
        
        
        int valor = 0;
        
        while(valor < 5){
            System.out.printf("valor é: %s \n", valor);
            valor++;
        }

	}
}
