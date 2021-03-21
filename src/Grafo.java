import java.util.*;

public class Grafo {

	/* definição de vertice */
	public class Vertice {
		
		String nome;
		List<Aresta> adj;
		Double d;
		Vertice pai;
		String cor;

		/* contrutor de um vértice padrão */
		Vertice(String nome) {
			this.nome = nome;
			this.adj = new ArrayList<Aresta>();
		}

		/* contrutor de um vertice nulo para inicialização */
		Vertice() {
			this.nome = "Nulo";
		}

		/* adiciona uma aresta na lista de adjacencia */	
		void addAdj(Aresta e) {
			adj.add(e);
		}

	}

	/* definição de aresta - com e sem peso */
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

	/* contrutor da classe grafo */
	public Grafo() {
		vertices = new ArrayList<Vertice>();
		arestas = new ArrayList<Aresta>();
	}

	/* adiciona um vértice à lista de vértices */
	public Vertice addVertice(String nome) {
		Vertice v = new Vertice(nome);
		vertices.add(v);
		return v;
	}

	/* Adiciona aresta de um grafo não ponderado */
	public Aresta addAresta(Vertice origem, Vertice destino){
		Aresta e =  new Aresta(origem, destino);
		origem.addAdj(e);
		arestas.add(e);
		return e;
	}

	/* Adiciona aresta de um grafo ponderado */
	public Aresta addAresta(Vertice origem, Vertice destino, Integer peso) {
		Aresta e = new Aresta(origem, destino, peso);
		origem.addAdj(e);
		arestas.add(e);
		return e;
	}

	
	/*
	BFS
	- Calcula a distância de todos os vertíces alcançáveis 
	a partir de um vértice de origem s;
	- Bfs(grafo, vertice) => vertice com a maior distancia.
	*/
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

	/*
	função diâmetro
	- comprimento do maior caminho do grafo;
	- diametro(grafo, vertice) => maior comprimento do grafo.
	*/
	public Double diametro(Grafo g, Vertice s) {

		Vertice a = new Vertice();
		Vertice b = new Vertice();
		
		a = g.Bfs(g, s);
		b = g.Bfs(g, a);

		return b.d;

	}
	
	/* testes unitarios */
	public void testesUnitarios(){
		//java -ea Grafo.java 		/* ativar o assert e emitir erros se houver */

		/*
		*---*     *---*     *---*
		| R |---->| S |     | T |
		*---*   />*---*     *---*
		  |    /    |      /  |  
		  v   /     v     /   v  
		*---*     *---*  /  *---*
		| V |<----| W |</  | X |
		*---*     *---*     *---*
		*/

		Grafo g = new Grafo();
		Vertice r = g.addVertice("r");
		Vertice s = g.addVertice("s");
		Vertice t = g.addVertice("t");
		Vertice v = g.addVertice("v");
		Vertice w = g.addVertice("w");
		Vertice x = g.addVertice("x");
		Aresta rs = g.addAresta(r, s);
		Aresta rv = g.addAresta(r, v);
		Aresta wv = g.addAresta(w, v);
		Aresta vs = g.addAresta(v, s);
		Aresta sw = g.addAresta(s, w);
		Aresta tw = g.addAresta(t, w);
		Aresta tx = g.addAresta(t, x);
		
		Double diametro1 = g.diametro(g, s);
		assert(diametro1 == 2.0) : "Erro no diametro 1";

		Double diametro2 = g.diametro(g, w);
		assert(diametro2 == 2.0) : "Erro no diametro 2";

		Double diametro3 = g.diametro(g, t);
		assert(diametro3 == 2.0) : "Erro no diametro 3";

		Double diametro4 = g.diametro(g, x);		
		assert(diametro4 == 0.0) : "Erro no diametro 4";

		Double diametro5 = g.diametro(g, v);
		assert(diametro5 == 2.0) : "Erro no diametro 5";


		/*
		*---*     *---*     
		| 1 |-----| 2 |     
		*---*   / *---* \    
		  |    /    |    \ *---*  
		  |   /     |      | 3 |
		*---*     *---*  / *---*
		| 5 |-----| 4 | /  
		*---*     *---*    
		*/

		Grafo h = new Grafo();
		Vertice v1 = h.addVertice("1");	
		Vertice v2 = h.addVertice("2");
		Vertice v3 = h.addVertice("3");
		Vertice v4 = h.addVertice("4");
		Vertice v5 = h.addVertice("5");
		Aresta e12 = h.addAresta(v1, v2);
		Aresta e15 = h.addAresta(v1, v5);
		Aresta e21 = h.addAresta(v2, v1);
		Aresta e23 = h.addAresta(v2, v3);
		Aresta e24 = h.addAresta(v2, v4);
		Aresta e25 = h.addAresta(v2, v5);
		Aresta e32 = h.addAresta(v3, v2);
		Aresta e34 = h.addAresta(v3, v4);
		Aresta e42 = h.addAresta(v4, v2);
		Aresta e43 = h.addAresta(v4, v3);
		Aresta e45 = h.addAresta(v4, v5);
		Aresta e51 = h.addAresta(v5, v1);
		Aresta e52 = h.addAresta(v5, v2);
		Aresta e54 = h.addAresta(v5, v4);

		Double diametro6 = h.diametro(h, v1);
		assert(diametro6 == 2.0) : "Erro no diametro 6";

		Double diametro7 = h.diametro(h, v2);
		assert(diametro7 == 2.0) : "Erro no diametro 7";

		Double diametro8 = h.diametro(h, v3);
		assert(diametro8 == 2.0) : "Erro no diametro 8";

		Double diametro9 = h.diametro(h, v4);
		assert(diametro9 == 2.0) : "Erro no diametro 9";

		Double diametro10 = h.diametro(h, v5);
		assert(diametro10 == 2.0) : "Erro no diametro 10";

	}


	public static void main(String[] args){
		Grafo g = new Grafo();
		g.testesUnitarios();
	}

}