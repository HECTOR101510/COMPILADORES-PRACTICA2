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
        stack.push("$");//llenamos con fin de cadena
        stack.push("Q");//se llena con el primer elemento
        String x=stack.peek();//apuntamos el tope de la pila
        while (x!="EOF") {
            if(x.equals(preanalisis.tipo.name())){//x apunta un terminal de la cadena metida
                stack.pop();//Eliminamos lo que esta arriba de la pila
                x=stack.peek();//apuntamos en lo mas alto de la pila
                i++;
                preanalisis=this.tokens.get(i);
            }

            else if(x=="Q"){
                stack.pop();
                stack.push("T");
                stack.push("FROM");
                stack.push("D");
                stack.push("select");
                x=stack.peek();
            }
            else if(x=="D"){
                stack.pop();
                if(preanalisis.lexema=="distinct"){
                    stack.push("P");
                    stack.push("distinct");
                    x=stack.peek();
                }else{
                    stack.push("P");
                    x=stack.peek();
                }
            }
            else if(x=="P"){
                stack.pop();
                if(preanalisis.lexema=="*"){
                    stack.push("*");
                    x=stack.peek();
                }else{
                    stack.push("A");
                    x=stack.peek();
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
            }
        }
    }
    @Override
    public boolean parse(){
        analisis();
        return !hayErrores;
    }
}