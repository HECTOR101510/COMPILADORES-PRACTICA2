//Programa principal

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Principal {
    static boolean existenErrores=false;
    public static void main(String[] args)throws IOException{
         ejecutarPrompt();   
    }
    public static void ejecutarPrompt()throws IOException{
        InputStreamReader input= new InputStreamReader(System.in);
        BufferedReader reader= new BufferedReader(input);

        for(;;){
            System.out.print(">>>");
            String linea=reader.readLine();
            if(linea==null) break;
            ejecutar(linea);
            existenErrores=false;
        }
    }
    public static void ejecutar(String source){
       Scanner scanner= new Scanner(source);
        List<Token> tokens= scanner.scanTokens();
        for(Token token : tokens){
            System.out.println(token);
        }

        Parser parser=new ASDI(tokens);
        parser.parse();
        
    }
}