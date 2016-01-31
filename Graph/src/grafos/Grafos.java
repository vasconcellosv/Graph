package grafos;

import java.io.IOException;
import org.jgraph.*;

public class Grafos {
    
    public static void main(String[] args) throws IOException {
        //GeradorGrafoAleatorio g = new GeradorGrafoAleatorio(4, 0.4);
        //g.GerarGrafoAleatorio();
            
        LeitorGrafo l = new LeitorGrafo();
        l.Imprime(l.LeGrafo());
        
        BuscaEmProfundidade busca = new BuscaEmProfundidade(l.grafoLido());
        busca.bep();
    }    
}
