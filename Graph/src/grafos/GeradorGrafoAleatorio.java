package grafos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

public class GeradorGrafoAleatorio {

    int numeroDeVertices;
    double probabilidade;

    public GeradorGrafoAleatorio(int n, double p) {
        this.numeroDeVertices = n;
        this.probabilidade = p;
    }

    public Graph<Object, DefaultEdge> GerarGrafoAleatorio() throws IOException {
        //Cria o Grafo
        Graph<Object, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        System.out.println("----------------------------------------- Vertices e Suas Arestas -----------------------------------------");

        //Adiciona os Vertices
        for (int i = 0; i < numeroDeVertices; i++) {
            g.addVertex(new Vertice(i));
            System.out.println(i);
        }
        //Adiciona as arestas aleatoriamente
        for (int i = 0; i < numeroDeVertices; i++) {
            for (int j = i + 1; j < numeroDeVertices; j++) {
                if (Math.random() < probabilidade) {
                    g.addEdge(buscaVertice(g, i), buscaVertice(g, j));
                }
            }
        }
        Iterator<Object> iterador = new DepthFirstIterator<>(g);
        Object v;
        Vertice t;
        while (iterador.hasNext()) {
            v = iterador.next();
            t = (Vertice) v;
            System.out.println(t.getId() + " -> " + g.edgesOf(t).toString().replace("grafos.Vertice@", ""));
        }
        System.out.println("----------------------------------------- O Que Foi Escrito no Arquivo -----------------------------------------");
        exportaGrafo(g);
        return g;
    }

    public Object buscaVertice(Graph<Object, DefaultEdge> g, int i) {

        for (Object v : g.vertexSet()) {
            if(v.equals(i)){
                return v;
            }
        }
        return null;
    }

    public void exportaGrafo(Graph<Object, DefaultEdge> g) throws IOException {

        File arquivo = new File("..\\Graph\\arquivos\\grafo.txt");
        FileWriter file = new FileWriter(arquivo);

        Iterator<Object> iterador = new DepthFirstIterator<>(g);
        Object v;
        Vertice t;
        while (iterador.hasNext()) {
            v = iterador.next();
            t = (Vertice) v;
            System.out.print(t.getId() + " ");
            file.write(t.getId() + " ");
        }

        System.out.println("");
        file.write(System.lineSeparator());

        System.out.println(g.edgeSet().toString().replace("grafos.Vertice@", ""));
        file.write(g.edgeSet().toString().replace("grafos.Vertice@", ""));

        file.close();
    }
}
