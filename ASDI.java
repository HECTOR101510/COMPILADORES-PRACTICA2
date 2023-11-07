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
            }
            else if(x.equals("Q")){
                stack.pop();
                stack.push("T");
                stack.push("FROM");
                stack.push("D");
                stack.push("SELECT");
                x=stack.peek();
            }
            else if(x.equals("D")){
               if(preanalisis.tipo.name().equals("DISTINCT")){stack.pop();
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
            else if(x.equals("A2")){
                if(preanalisis.tipo.name().equals("IDENTIFICADOR")){
                    stack.pop();
                    stack.push("A3");
                    stack.push("IDENTIFICADOR");
                    x=stack.peek();
                }
                else{
                    System.out.println("Error: Se esperaba un id");
                    hayErrores = true;
                    break;
                }
            }
            else if(x.equals("A3")){
                if(preanalisis.tipo.name().equals("FROM")||preanalisis.tipo.name().equals("COMA")){//cadeba vacia
                    stack.pop();
                    x=stack.peek();
                }
                else if(preanalisis.tipo.name().equals("PUNTO")){
                    stack.pop();
                    stack.push("IDENTIFICADOR");
                    stack.push("PUNTO");
                    x=stack.peek();
                }
                else{
                    System.out.println("Error: Se esperaba un id o un from o un .");
                    hayErrores = true;
                    break;
                }
            }
            else if(x.equals("T")){
                if(preanalisis.tipo.name().equals("IDENTIFICADOR")){
                    stack.pop();
                    stack.push("T1");
                    stack.push("T2");
                    x=stack.peek();
                }
                else{
                    System.out.println("Error: Se esperaba un id");
                    hayErrores = true;
                    break;
                }
            }
            else if(x.equals("T1")){
               if(preanalisis.tipo.name().equals("COMA")){
                    stack.pop();
                    stack.push("T");
                    stack.push("COMA");
                    x=stack.peek();
                }
                else if(preanalisis.tipo.name().equals("EOF")){
                    stack.pop();
                    x=stack.peek();
                }
                else{
                    System.out.println("Error: Se esperaba una ,");
                    hayErrores = true;
                    break;
                }
            }
            else if(x.equals("T2")){
               if(preanalisis.tipo.name().equals("IDENTIFICADOR")){
                    stack.pop();
                    stack.push("T3");
                    stack.push("IDENTIFICADOR");
                    x=stack.peek();
                }
                else{
                    System.out.println("Error: Se esperaba un id");
                    hayErrores = true;
                    break;
                }
            }
            else if(x.equals("T3")){
               if(preanalisis.tipo.name().equals("COMA")){
                    stack.pop();
                    x=stack.peek();
                }
                else if(preanalisis.tipo.name().equals("IDENTIFICADOR")){
                    stack.pop();
                    stack.push("IDENTIFICADOR");
                    x=stack.peek();
                }
                else if(preanalisis.tipo.name().equals("EOF")){
                    stack.pop();
                    x=stack.peek();
                }
                else{
                    System.out.println("Error: Se esperaba un id");
                    hayErrores = true;
                    break;
                }
            }
            else{
                System.out.println("Se ha encontrado errores: ");
                hayErrores = true;
                break;
            }
        }
    }
    @Override
    public boolean parse(){
        analisis();
        return !hayErrores;
    }
}