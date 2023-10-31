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

        while (x!="$") {
            if(x.equals(preanalisis.lexema)){//x apunta un terminal de la cadena metida
                stack.pop();//Eliminamos lo que esta arriba de la pila
                x=stack.peek();//apuntamos en lo mas alto de la pila
                i++;
                preanalisis=this.tokens.get(i);
            }

            else if(x=="Q"){
                stack.pop();
                stack.push("T");
                stack.push("from");
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
            else{
                System.out.println("Se ha encontrado errores: ");
            }
        }
    }



    @Override
    public boolean parse(){
        return false;
    }
}

