package grafos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

public class LeitorGrafo {

    public Graph<Object, DefaultEdge> LeGrafo() throws FileNotFoundException {
        int origem;
        int destino;
        
        Queue<String> list = new LinkedList<String>();
        
        Graph<Object, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        
        File arquivo = new File("..\\Grafos\\arquivos\\grafo.txt");
        Scanner file = new Scanner(arquivo);
        
        while(file.hasNextInt()){
            g.addVertex(new Vertice(file.nextInt()));
        }
        Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)");
        Matcher match = null;
        while(file.hasNext()){
            match = pattern.matcher(file.nextLine());
            while(match.find()){
                //System.out.println(match.group(1));
                list.add(match.group(1));                
            }
        }
        while(!list.isEmpty()){
            origem = Integer.parseInt(list.poll());
            destino = Integer.parseInt(list.poll());
            
            g.addEdge(buscaVertice(g, origem), buscaVertice(g, destino));
        }
        
        return g;
    }
    
    public void Imprime(Graph<Object, DefaultEdge> g){
        System.out.println("----------------------------------------- O Que Foi Lido do Arquivo -----------------------------------------");
        Iterator<Object> iterador = new DepthFirstIterator<>(g);
        Object v;
        Vertice t;
        while (iterador.hasNext()) {
            v = iterador.next();
            t = (Vertice) v;
            System.out.println(t.getId() + " -> " + g.edgesOf(t).toString().replace("grafos.Vertice@", ""));
        }
    }
    public Object buscaVertice(Graph<Object, DefaultEdge> g, int i) {

        for (Object v : g.vertexSet()) {
            if(v.equals(i)){
                return v;
            }
        }
        return null;
    }
}
