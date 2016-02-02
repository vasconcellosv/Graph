package grafos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class BuscaEmProfundidade {

    int nmComponentesConexas = 0;
    int tempo = 0;

    boolean biPartido = true;

    Graph<Object, DefaultEdge> g;
    Vertice raiz = null;

    List<DefaultEdge> circuito = new ArrayList<DefaultEdge>();
    ArrayList<List<DefaultEdge>> ArvoreProfundidade = new ArrayList<List<DefaultEdge>>();
    List<Vertice> raizes = new ArrayList<Vertice>();
    List<DefaultEdge> tempArvoreProfundidade = new ArrayList<DefaultEdge>();
    List<DefaultEdge> tempComponenteConexa = new ArrayList<DefaultEdge>();
    ArrayList<List<DefaultEdge>> ComponenteConexa = new ArrayList<List<DefaultEdge>>();
    ArrayList<List<DefaultEdge>> ciclos = new ArrayList<List<DefaultEdge>>();
    List<DefaultEdge> arestaDeRetorno = new ArrayList<DefaultEdge>();
    List<Integer> profundidadeSaida = new ArrayList<Integer>();
    List<Stack<DefaultEdge>> blocos = new ArrayList<Stack<DefaultEdge>>();
    List<Vertice> articulacoes = new ArrayList<Vertice>();
    List<DefaultEdge> pontes = new ArrayList<DefaultEdge>();
    //List<List<Vertice>> componentesConexas = new ArrayList<List<Vertice>>();
    Stack<DefaultEdge> tempBloco = new Stack<DefaultEdge>();

    public BuscaEmProfundidade(Graph<Object, DefaultEdge> g) {
        this.g = g;
    }

    public void bep() {
        while (temPeZero(g)) {
            nmComponentesConexas++;
            raiz = escolheRaizParaIniciar();
            raizes.add(raiz);
            busca(raiz);
            profundidadeSaida.add(maiorPS());
            ArvoreProfundidade.add(tempArvoreProfundidade);
            ComponenteConexa.add(tempComponenteConexa);
            tempArvoreProfundidade = new ArrayList<DefaultEdge>();
            tempComponenteConexa = new ArrayList<DefaultEdge>();
        }

    }

    public void busca(Vertice v) {
        tempo = tempo + 1;
        v.setpE(tempo);
        v.setBack(v.getpE());

        for (Vertice w : filhos(v)) {
            if (w.getpE() == 0) {
                tempArvoreProfundidade.add(getAresta(v, w));
                tempComponenteConexa.add(getAresta(v, w));
                w.setPai(v);
                busca(w);
                if (w.getBack() >= v.getpE()) {
                    if (!articulacoes.contains(v) && ((!raiz.equals(v)) || filhosNaArvore(v).size() >= 2)) {
                        articulacoes.add(v);
                    }
                    tempBloco.add(getAresta(v, w));
                    blocos.add(tempBloco);
                    if (tempBloco.size() == 1) {
                        pontes.add(getAresta(v, w));
                    }
                    tempBloco = new Stack<DefaultEdge>();
                } else {
                    tempBloco.add(getAresta(v, w));
                }
                v.setBack(Math.min(v.getBack(), w.getBack()));
            } else if (w.getpS() == 0 && (!v.getPai().equals(w))) {
                arestaDeRetorno.add(getAresta(v, w));
                ciclos.add(ciclo(v, w));
                tempComponenteConexa.add(getAresta(v, w));
                tempBloco.add(getAresta(v, w));
                v.setBack(Math.min(v.getBack(), w.getpE()));
                cicloImpar(v, w);
            }
        }
        tempo = tempo + 1;
        v.setpS(tempo);
    }

    public void imprimeInformacoes() {
        System.out.println("----------------------------------------- Informações sobre o Grafo -----------------------------------------");

        if (nmComponentesConexas == 1) {
            System.out.println("O Grafo é Conexo.");
        } else {
            System.out.println("O Grafo não é conexo.");
        }

        System.out.println("");

        System.out.println("Número de Arvores da Floresta Geradora: " + nmComponentesConexas);
        int i = 0;
        for (List<DefaultEdge> componente : ArvoreProfundidade) {
            System.out.print("Arvore : " + raizes.get(i).toString().replace("grafos.Vertice@", "") + " -> " + componente.toString().replace("grafos.Vertice@", ""));
            i++;
            System.out.println("");
        }
        System.out.println("");

        System.out.println("");

        if (arestaDeRetorno.size() > 0) {
            System.out.println("Possui Cíclo.");
            i = 1;
            for (List<DefaultEdge> ciclo : ciclos) {
                System.out.print("Ciclo : " + i + " -> " + ciclo.toString().replace("grafos.Vertice@", ""));
            i++;
            System.out.println("");
            }
        } else {
            System.out.println("Não Possui Ciclo.");
        }

        System.out.println("");

        if (biPartido) {
            System.out.println("É BiPartido.");
        } else {
            System.out.println("Não é BiPartido.");
        }

        System.out.println("");

        System.out.println("Número de Componentes Conexas: " + nmComponentesConexas);
        i = 1;
        for (List<DefaultEdge> componente : ComponenteConexa) {
            System.out.print("Componente Conexa : " + i + " -> " + componente.toString().replace("grafos.Vertice@", ""));
            i++;
            System.out.println("");
        }
        System.out.println("");

        System.out.println("");

        System.out.println("Número de Pontes: " + pontes.size());
        for (DefaultEdge ponte : pontes) {
            System.out.print(ponte.toString().replace("grafos.Vertice@", ""));
        }
        System.out.println("");

        System.out.println("");

        System.out.println("Número de Articulações: " + articulacoes.size());
        for (Vertice articulacao : articulacoes) {
            System.out.println("Vertice: " + articulacao.getId());
        }
        System.out.println("");

        System.out.println("");

        System.out.println("Número de Blocos: " + blocos.size());
        i = 1;
        for (List<DefaultEdge> bloco : blocos) {
            System.out.print("Bloco : " + i + " -> " + bloco.toString().replace("grafos.Vertice@", ""));
            i++;
            System.out.println("");
        }
        System.out.println("");
        
        System.out.println("");
        
        if(new Euleriano(g).ehEuleriano(nmComponentesConexas)){
            System.out.println("É Euleriano.");
            circuitoEuleriano();
            System.out.println(circuito.toString().replace("grafos.Vertice@", ""));
        }else{
            System.out.println("Não é Euleriano.");
        }
    }

    public boolean temPeZero(Graph<Object, DefaultEdge> g) {
        for (Object v : g.vertexSet()) {
            Vertice t = (Vertice) v;
            if (t.getpE() == 0) {
                return true;
            }
        }
        return false;
    }

    public Vertice escolheRaizParaIniciar() {
        for (Object v : g.vertexSet()) {
            Vertice t = (Vertice) v;
            //if (t.getpE() == 0) {
            //    return t;
            //}
            if(t.getId() == 4){
                return t;
            }
        }
        return null;
    }

    public Vertice escolheRaiz() {
        for (Object v : g.vertexSet()) {
            Vertice t = (Vertice) v;
            return t;
        }
        return null;
    }

    private List<Vertice> filhos(Vertice v) {

        List<Vertice> list = new ArrayList<>();
        Set<DefaultEdge> arestas = g.edgesOf(v);

        for (DefaultEdge aresta : arestas) {
            if (g.getEdgeSource(aresta).equals(v)) {
                list.add((Vertice) g.getEdgeTarget(aresta));
            } else if (g.getEdgeTarget(aresta).equals(v)) {
                list.add((Vertice) g.getEdgeSource(aresta));
            }
        }
        return list;
    }

    private List<Vertice> filhosNaArvore(Vertice v) {

        List<Vertice> list = new ArrayList<>();

        for (DefaultEdge aresta : tempArvoreProfundidade) {
            if (g.getEdgeSource(aresta).equals(v)) {
                list.add((Vertice) g.getEdgeTarget(aresta));
            } else if (g.getEdgeTarget(aresta).equals(v)) {
                list.add((Vertice) g.getEdgeSource(aresta));
            }
        }
        return list;
    }

    private DefaultEdge getAresta(Vertice v, Vertice w) {
        DefaultEdge aresta = null;
        aresta = (DefaultEdge) g.getEdge(v, w);
        return aresta;
    }

    private void cicloImpar(Vertice v, Vertice w) {
        int count = 1;
        Vertice temp = v;
        while (!temp.getPai().equals(w)) {
            count++;
            temp = temp.getPai();
        }
        if (count % 2 != 0) {
            biPartido = false;
        }
    }
    
    private List<DefaultEdge> ciclo(Vertice v, Vertice w){
        
        List<DefaultEdge> lista = new ArrayList<DefaultEdge>();
        int count = 1;
        Vertice temp = v;
        lista.add(getAresta(temp, w));
        
        while (!temp.getPai().equals(w)) {
            lista.add(getAresta(temp, temp.getPai()));
            temp = temp.getPai();
        }
        lista.add(getAresta(temp, temp.getPai()));
        
        return lista;
    }

    private Integer maiorPS() {
        int maior = 0;
        for (Object v : g.vertexSet()) {
            Vertice t = (Vertice) v;
            if (t.getpS() > maior) {
                maior = t.getpS();
            }
        }
        return maior;
    }

    private Vertice outroVertice(DefaultEdge aresta, Vertice v) {

        if (g.getEdgeSource(aresta).equals(v)) {
            return ((Vertice) g.getEdgeTarget(aresta));
        } else if (g.getEdgeTarget(aresta).equals(v)) {
            return ((Vertice) g.getEdgeSource(aresta));
        }

        return null;
    }

    public List<DefaultEdge> circuitoEuleriano() {

        List<DefaultEdge> arestasVisitadas = new ArrayList<DefaultEdge>();
        List<Vertice> verticesVisitados = new ArrayList<Vertice>();
        Queue<DefaultEdge> arestas = new LinkedList<DefaultEdge>();
        boolean sai = true;

        Euleriano euleriano = new Euleriano(g);
        if (euleriano.ehEuleriano(nmComponentesConexas)) {

            Vertice v = escolheRaiz();
            verticesVisitados.add(v);

            while (!circuito.containsAll(g.edgeSet())) {

                //Primeiro pegas as arestas que não são pontes
                for (DefaultEdge aresta : g.edgesOf(v)) {
                    if (!pontes.contains(aresta) && !verticesVisitados.containsAll(filhos(outroVertice(aresta, v)))) {
                        arestas.add(aresta);
                    }
                }
                //Depois as que são pontes
                for (DefaultEdge aresta : g.edgesOf(v)) {
                    if (pontes.contains(aresta) && !verticesVisitados.containsAll(filhos(outroVertice(aresta, v)))) {
                        arestas.add(aresta);
                    }
                }

                for (DefaultEdge aresta : g.edgesOf(v)) {
                    if (verticesVisitados.containsAll(filhos(outroVertice(aresta, v)))) {
                        arestas.add(aresta);
                    }
                }

                DefaultEdge aresta = new DefaultEdge();

                while (!arestas.isEmpty() && sai) {
                    aresta = arestas.poll();

                    if (!arestasVisitadas.contains(aresta)) {
                        sai = false;
                    }
                }
                sai = true;

                if (!arestasVisitadas.contains(aresta)) {
                    arestasVisitadas.add(aresta);
                    circuito.add(aresta);
                    if (!g.getEdgeTarget(aresta).equals(v)) {
                        v = (Vertice) g.getEdgeTarget(aresta);
                    } else {
                        v = (Vertice) g.getEdgeSource(aresta);
                    }
                    verticesVisitados.add(v);
                    arestas.clear();
                }
            }

        }
        return circuito;
    }

}
