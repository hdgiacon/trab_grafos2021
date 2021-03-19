package Grafo;

import java.util.List;
import java.util.ArrayList;

public class Grafo {
	public class Vertice {
		String nome;
		List<Aresta> adj;

		Vertice(String nome) {
			this.nome = nome;
			this.adj = new ArrayList<Aresta>();
		}

		void addAdj(Aresta e) {
			adj.add(e);
		}
	}

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

}
