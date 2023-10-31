import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final List<Token> tokens= new ArrayList<>();
    private static final Map<String, TipoToken> palabrasReservadas;
    
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("select", TipoToken.SELECT);
        palabrasReservadas.put("from", TipoToken.FROM);
        palabrasReservadas.put("distinct", TipoToken.DISTINCT);
        palabrasReservadas.put("*",TipoToken.ASTERISCO);
        palabrasReservadas.put(",",TipoToken.COMA);
        palabrasReservadas.put(".",TipoToken.PUNTO);
    }
    public boolean terminal(char c){
        String caracteres=",.*";
        return caracteres.indexOf(c)!=-1;
    }
    Scanner(String source){
        this.source=source+" ";
    }
    List<Token> scanTokens(){
        String lexema="";
        int estado=0;
        char c;
        for(int i=0; i<source.length(); i++){
            c=source.charAt(i);
            switch (estado) {
                case 0:
                    if(terminal(c)){
                        i--;
                        estado=1;
                    }
                    else if(Character.isLetter(c)||Character.isDigit(c)){
                        lexema+=c;
                        estado=2;
                    }
                    break;
                    case 1:
                        if(c=='*'){
                            lexema+=c;
                            Token t=new Token(TipoToken.ASTERISCO, lexema);
                            estado=0;
                            lexema="";
                            tokens.add(t);
                        }
                        else if (c=='.') {
                            lexema+=c;
                            Token t=new Token(TipoToken.PUNTO, lexema);
                            estado=0;
                            lexema="";
                            tokens.add(t);
                        }
                        else if (c==',') {
                            lexema+=c;
                            Token t=new Token(TipoToken.COMA, lexema);
                            estado=0;
                            lexema="";
                            tokens.add(t);
                        }
                        break;
                    case 2:
                        if(Character.isLetter(c)||Character.isDigit(c)){
                            estado=2;
                            lexema+=c;
                        }
                        else{
                            TipoToken tt=palabrasReservadas.get(lexema);
                            if(tt==null){
                                Token t=new Token(TipoToken.IDENTIFICADOR, lexema);
                                tokens.add(t);
                            }
                            else{
                                Token t=new Token(tt, lexema);
                                tokens.add(t);
                            }
                            estado=0;
                            lexema="";
                            i--;
                        }
                        break;
            }
        }
        tokens.add(new Token(TipoToken.EOF, lexema));
        return tokens;
    }
}
