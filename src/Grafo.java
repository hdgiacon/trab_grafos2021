import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.*;

public class Grafo {

	/*definição de vertice */
	public class Vertice {
		String nome;
		List<Aresta> adj;
		Double d;
		Vertice pai;
		String cor;

		Vertice(String nome) {
			this.nome = nome;
			this.adj = new ArrayList<Aresta>();
		}

		Vertice() {
			this.nome = "Nulo";
		}

		void addAdj(Aresta e) {
			adj.add(e);
		}
	}

	/*definição de aresta => com e sem peso*/
	public class Aresta {
		Vertice origem;
		Vertice destino;
		Integer peso;

		Aresta(Vertice origem, Vertice destino) {
			this.origem = origem;
			this.destino = destino;
		}

		Aresta(Vertice origem, Vertice destino, Integer peso) {
			this.origem = origem;
			this.destino = destino;
			this.peso = peso;
		}
	}

	List<Vertice> vertices;
	List<Aresta> arestas;

	/*contrutor da classe grafo*/
	public Grafo() {
		vertices = new ArrayList<Vertice>();
		arestas = new ArrayList<Aresta>();
	}

	public Vertice addVertice(String nome) {
		Vertice v = new Vertice(nome);
		vertices.add(v);
		return v;
	}

	public Aresta addAresta(Vertice origem, Vertice destino){
		Aresta e =  new Aresta(origem, destino);
		origem.addAdj(e);
		arestas.add(e);
		return e;
	}

	public Aresta addAresta(Vertice origem, Vertice destino, Integer peso) {
		Aresta e = new Aresta(origem, destino, peso);
		origem.addAdj(e);
		arestas.add(e);
		return e;
	}

	
	/*BFS*/
	public Vertice Bfs(Grafo g, Vertice s) {
		for(Vertice v: g.vertices){
			v.d = Double.POSITIVE_INFINITY;
			v.pai = new Vertice();
			v.cor = "Branco";
		}

		s.d = 0.0;
		s.pai = new Vertice();
		s.cor = "Cinza";
		Deque<Vertice> Q = new ArrayDeque<Vertice>();
		Q.add(s);

		Vertice u = new Vertice("u");
		Vertice maior = new Vertice("maior");
		maior = s;

		while(Q.size() != 0) {
			u = Q.removeFirst();
			for(Aresta v: u.adj) {
				if(v.destino.cor == "Branco") {
					v.destino.cor = "Cinza";
					v.destino.pai = u;
					v.destino.d = u.d + 1.0;
					Q.add(v.destino);

					if(v.destino.d > maior.d){
						maior = v.destino;
					}
				}
			}
		}
		
		return maior;
	}

	/*função diâmetro*/
	public Double diametro(Grafo g) {
		Vertice s = g.vertices.get(0);
		Vertice a = new Vertice();
		Vertice b = new Vertice();
		
		a = g.Bfs(g, s);
		b = g.Bfs(g, a);
		
		return b.d - a.d;

		// s = vértice qualquer de G.V
		// a = vértice c/ valor maximo de d obtido pelo BFS(G, s)
		// b = vértice c/ valor maximo de d obtido pelo BFS(G, a)
		// return distancia entre a e b
	}
	
	/*testes unitarios*/
	public void testesUnitarios(){
		//java -ea Grafo.java para ativar o assert e emitir erros se houver
		assert (false) : "mensagem de erro";
	}


	public static void main(String[] args){
		Grafo g = new Grafo();
		Vertice s = g.addVertice("s");
		Vertice t = g.addVertice("t");
		Vertice u = g.addVertice("u");
		Aresta st = g.addAresta(s, t);
		Aresta su = g.addAresta(s, u);
		
		g.Bfs(g, s);
		for(Vertice v: g.vertices) {
			//System.out.println(v.cor);
			//System.out.println(v.d);
			//System.out.println(v.pai.nome);
		}
		
		g.diametro(g);
	}

}
