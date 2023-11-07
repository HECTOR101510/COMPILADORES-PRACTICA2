//hola aqui el proyecto

import java.util.List;
import java.util.Stack;

public class ASDI implements Parser{
    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    
    public ASDI(List<Token> tokens){
        this.tokens=tokens;
        preanalisis=this.tokens.get(i);
    }

    public void analisis(){
        Stack<String> stack = new Stack<>();//creamos la pila
        stack.push("EOF");//llenamos con fin de cadena
        stack.push("Q");//se llena con el primer elemento
        String x=stack.peek();//apuntamos el tope de la pila

        while (x!="EOF") {
           if(x.equals(preanalisis.tipo.name())){//x apunta un terminal de la cadena metida
                stack.pop();//Eliminamos lo que esta arriba de la pila
                x=stack.peek();//apuntamos en lo mas alto de la pila
                i++;
                preanalisis=this.tokens.get(i);
                System.out.println("Entramos en if verdadero");
                System.out.println("Prenalisis vale:"+preanalisis);
                System.out.println("valor de x:"+x);
            }
            else if(x.equals("Q")){
                System.out.println("Entramos en Q");
                stack.pop();
                stack.push("T");
                stack.push("FROM");
                stack.push("D");
                stack.push("SELECT");
                x=stack.peek();
                System.out.println("Salimos de Q con valor de x:"+x);
                System.out.println("Prenalisis vale:"+preanalisis);
            }
            else if(x.equals("D")){
                System.out.println("entrnado en D");
               if(preanalisis.tipo.name().equals("DISTINCT")){
                System.out.println("entrnado en D primer if x:");
                    stack.pop();
                    stack.push("P");
                    stack.push("DISTINCT");
                    x=stack.peek();
               }
               else if(preanalisis.tipo.name().equals("ASTERISCO")||preanalisis.tipo.name().equals("IDENTIFICADOR")){
                    stack.pop();
                    stack.push("P");
                    x=stack.peek();
               }
               else{
                System.out.println("Error: Se esperaba un id o * o distinct");
                hayErrores = true; // Marca que se ha encontrado un error
                break;
               }
            }
            else if(x.equals("P")){
                System.out.println("El valor de preanalisis: "+preanalisis.tipo.name());
                if(preanalisis.tipo.name().equals("ASTERISCO")){
                    stack.pop();
                    stack.push("ASTERISCO");
                    x=stack.peek();
                }
                else if(preanalisis.tipo.name().equals("IDENTIFICADOR")){
                    stack.pop();
                    stack.push("A");
                    x=stack.peek();
                }
                else{
                System.out.println("Error: Se esperaba un id o *");
                hayErrores = true;
                break;
               }
            }
            else if(x.equals("A")){
                if(preanalisis.tipo.name().equals("IDENTIFICADOR")){
                    stack.pop();
                    stack.push("A1");
                    stack.push("A2");
                    x=stack.peek();
                }
                else{
                    System.out.println("Error: Se esperaba un id");
                    hayErrores = true;
                    break;
                }
            }
            else if(x.equals("A1")){
                if(preanalisis.tipo.name().equals("FROM")){//cadeba vacia
                    stack.pop();
                    x=stack.peek();
                }
                else if(preanalisis.tipo.name().equals("COMA")){
                    stack.pop();
                    stack.push("A");
                    stack.push("COMA");
                    x=stack.peek();
                }
                else{
                    System.out.println("Error: Se esperaba una ',' o from");
                    hayErrores = true;
                    break;
                }
            }


    @Override
    public boolean parse(){
        return false;
    }
}

