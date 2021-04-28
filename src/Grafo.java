/*

	Gustavo Belançon Mendes, RA: 99037
	Héctor Dorrighello Giacon, RA: 99450

*/

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/* definição de grafo */
public class Grafo {

	/* contantes para cor */
	static final String BRANCO = "branco";
	static final String CINZA = "cinza";

	
	/* definição de vertice */
	public class Vertice {
		
		int nome;
		List<Vertice> adj;
		Double d;
		Vertice pai;
		String cor;
		boolean visitado;
		int rank;
		Double chave;

		/* construtor de um vértice padrão */
		Vertice(int nome) {
			this.nome = nome;
			this.adj = new ArrayList<Vertice>();
		}

		/* construtor de um vertice nulo para inicialização */
		Vertice() {
			this.nome = -1;
		}

		/* adiciona um vértice na lista de adjacencia */	
		void addAdj(Vertice v) {
			adj.add(v);
		}

	}

	/*
	 * faz com que os nós da áevore apontem para a raiz;
	 * 
	 * findSet(x) => x.pai;
	 *
	 * findSet(v) => v.pai = u
	*/
	Vertice findSet(Vertice x) {
		if(x.nome != x.pai.nome) {
			x.pai = findSet(x.pai);
		}
		return x.pai;
	}

	/*
	 * inicializa os vértices da árvore;
	 * 
	 * makeSet(x) => efeito colateral sobre o grafo no qual os valores de pai são setados para o próprio
	  vértice e rank para 0;
	 *
	 * makeSet(u).
	*/
	void makeSet(Vertice x) {
		x.pai = x;
		x.rank = 0;
	}

	void link(Vertice x, Vertice y) {
		if(x.rank > y.rank) {
			y.pai = x;
		}
		else {
			x.pai = y;
			if(x.rank == y.rank) {
				y.rank = y.rank + 1;
			}
		}
	}



	/*	
	 * Define um rank para cada vértice e ordena a árvore beaseado nesse atributo rank;
	 * Rank determina a altura de um determinado vértice na árvore;
	 *
	 * union(vertice, vertice) => possui efeito colateral sobre o grafo no qual os vertices 
	   são ordenados pelo seu rank, no qual o pai tem sempre o maior rank;
	 *
	 * union(u, v);
	*/
	void union(Vertice x, Vertice y) {
		link(findSet(x), findSet(y));
	}

	/* definição de aresta */
	public class Aresta {
		
		Vertice origem;
		Vertice destino;
		Double peso;

		/* construtor de uma aresta */
		Aresta(Vertice origem, Vertice destino) {
			this.origem = origem;
			this.destino = destino;
		}

		/* construtor de uma aresta com peso */
		Aresta(Vertice origem, Vertice destino, Double peso) {
            this.origem = origem;
            this.destino = destino;
            this.peso = peso;
        }

		public Double getPeso() {
			return this.peso;
		}

	}

	List<Vertice> vertices;
	List<Aresta> arestas;
	Double lista_adj[][];

	/* contrutor da classe grafo */
	public Grafo() {
		vertices = new ArrayList<Vertice>();
		arestas = new ArrayList<Aresta>();
	}

	/* adiciona um vértice à lista de vértices */
	public Vertice addVertice(int nome) {
		Vertice v = new Vertice(nome);
		vertices.add(v);
		return v;
	}

	/* Adiciona aresta orientada no grafo */
	public void addAresta(Vertice origem, Vertice destino) {
		Aresta e =  new Aresta(origem, destino);
		origem.addAdj(destino);
		arestas.add(e);
	}

	/* Adiciona aresta não orientada no grafo */
	public void addArestaNo(Vertice origem, Vertice destino) {
		Aresta e =  new Aresta(origem, destino);
		origem.addAdj(destino);
		destino.addAdj(origem);
		arestas.add(e);
	}

	public void addArestaNoPeso(Vertice origem, Vertice destino, Double peso) {
		Aresta e =  new Aresta(origem, destino, peso);
		origem.addAdj(destino);
		destino.addAdj(origem);
		arestas.add(e);
	}

	/*
	 * Calcula a distância de todos os vertíces alcançáveis 
	   a partir de um vértice de origem s;
	 * Resulta em uma arvore;
	 *
	 * Bfs(grafo, vertice) => resulta em um efeito colateral no grafo,
	   atribuindo a distância dos vértices v ao vértice de origem s no campo v.d,
	   seu vértice antecessor ao campo v.pai, e sua cor em v.cor.
	*/
	public void Bfs(Grafo g, Vertice s) {
		for(Vertice v: g.vertices){
			v.d = Double.POSITIVE_INFINITY;
			v.pai = new Vertice();
			v.cor = BRANCO;
		}

		s.d = 0.0;
		s.pai = new Vertice();
		s.cor = CINZA;
		Deque<Vertice> Q = new ArrayDeque<Vertice>();
		Q.add(s);

		Vertice u = new Vertice();

		while(Q.size() != 0) {
			u = Q.removeFirst();
			for(Vertice v: u.adj) {
				if(v.cor == BRANCO) {
					v.cor = CINZA;
					v.pai = u;
					v.d = u.d + 1.0;
					Q.add(v);
				}
			}
		}

	}

	/*	
	 * Função que retorna o vértice com maior distância;
	 * A entrada precisa ser uma árvore;
	 * Utilizado após o bfs para obter o vértice v com maior distancia do vértice u,
	   faz parte do cálculo do diametro do grafo;
	 * maior(grafo) => maior
	 *
	 * maior(G) => 3
	 * maior(H) => 2
	 * maior(I) => 1
	*/
	public Vertice maior(Grafo g) {
		Vertice maior = new Vertice();
		maior.d = 0.0;
		
		for(Vertice v: g.vertices) {
			
			if(v.d != Double.POSITIVE_INFINITY) {
				if(v.d >= maior.d) {
					maior = v;
				}
			}
		}

		return maior;
	}

	/*
	 * comprimento do maior caminho do grafo;
	 * entrada precisa ser uma árvore
	 * 
	 * diametro(grafo, vertice) => maior comprimento do grafo.
	 *
	 * diametro(G, s) => 2
	 * diametro(H, v1) => 3
	 * diametro(I, a) =>	1
	*/
	public Double diametro(Grafo g, Vertice s) {

		Vertice a = new Vertice();
		Vertice b = new Vertice();
		
		g.Bfs(g, s);
		a = maior(g);

		g.Bfs(g, a);
		b = maior(g);

		return b.d;

	}

	/*
	 * recebe um número inteiro n > 0; 
	 * produz uma árvore contendo os n vertices partindo de um vértice aleatório u.
	 *
	 * randomTreeRandomWalk(n) => G
	 * onde G é um grafo formado pela árvore.
	 *
	 * randomTreeRandomWalk(10) => árvore com 10 vértices
	 * randomTreeRandomWalk(32) => árvore com 32 vértices
	 * randomTreeRandomWalk(100) => árvore com 100 vértices
	*/
	public Grafo randomTreeRandomWalk(int n) {
		Grafo g = new Grafo();
		for(Integer i = 0; i < n; i++) {
			g.addVertice(i);
		}

		for(Vertice u: g.vertices) {
			u.visitado = false;
		}

		Random verticeAleatorio = new Random();

		Vertice u = g.vertices.get(0);
		u.visitado = true;

		while(g.arestas.size() < n-1) {
			Vertice v = g.vertices.get(verticeAleatorio.nextInt(n));

			if(v.visitado == false) {
				g.addArestaNo(u, v);
				v.visitado = true;
			}

			u = v;
		}

		return g;
	}

	/*
	 * verifica se um grafo G possui algum vértice de cor branco;
	 * se não houver, então G é conexo(true).
	 *
	 * isConnected(G) => true
	 * isConnected(G) => false
	*/
	boolean isConnected(Grafo G) {
		for(Vertice u: G.vertices){
			if(u.cor.equals(BRANCO)){
				return false;
			}
		}
		return true;
	}

	/*
	 * verifica se um grafo G é uma árvore;
	 * a condição if verifica;
	 * se não houver ciclos e o grafo for conexo, então G é uma árvore(true);
	 *
	 * isTree(G) => true;
	 * isTree(G) => false.
	*/
	boolean isTree(Grafo G) {
		if(G.arestas.size() != G.vertices.size() - 1){
			return false;
		}  
		Vertice h;
		h = G.vertices.get(0);
		Bfs(G, h);
		return isConnected(G);
	}

	/*
	 * A função grafoCompleto(int n) recebe um inteiro n e retorna um grafo g completo;
	 * O grafo retornado tem a caracteristica de que cada vértice é adjacente a todos os outros do grafo;
	 * Gera arestas com pesos aleatórios do tipo Double entre 0.0 e 1.0
	 * 
	 * grafoCompleto(n) => g completo com n vértices;
	 * 
	 * grafoCompleto(5) => g completo com 5 vértices;
	 * grafoCompleto(50) => g completo com 50 vértices.
	*/
	public Grafo grafoCompleto(int n) {
		Grafo g = new Grafo();
		for(Integer i = 0; i < n; i++) {
			g.addVertice(i);
		}

		Random gerador = new Random();
		Double peso;
		for(int i = 0;i< g.vertices.size();i++) {
			for(int j = i + 1; j < g.vertices.size();j++) {
				peso = gerador.nextDouble();
				g.addArestaNoPeso(g.vertices.get(i), g.vertices.get(j), peso);
			}
		}

		return g;
	}

	/*
	 * A função randomTreeKruskal(int n) recebe um inteiro n;
	 * retorna um grafo com as arestas da árvore formada por mst-kruskal;
	 * 
	 * randomTreeJruskal(n) => grafo de n vértices com arestas da árvore;
	 * 
	 * randomTreeJruskal(5) => grafo de 5 vértices com arestas da árvore;
	 * randomTreeJruskal(50) => grafo de 50 vértices com arestas da árvore.
	*/
	public Grafo randomTreeKruskal(int n) {
		Grafo g = grafoCompleto(n);

		g = mstKruskal(g);

		return g;
	}
	
	/*
	 * Constrói uma árvore A contendo as arestas de menor peso do grafo g;
	 * utiliza os métodos da estrutura union-find;
	 * 
	 * grafo => grafo A com arestas de menor peso;
	 * 
	 * g(10) => A(10);
	 * g(40) => A(40).
	 */
	public Grafo mstKruskal(Grafo g) {
		Grafo A = new Grafo();

		for(Vertice v: g.vertices) {
			v.pai = A.addVertice(v.nome);
			makeSet(v);
		}

		// ordenar arestas por ordem crescente de peso (não decrescente)
		g.arestas.sort(Comparator.comparingDouble(Aresta::getPeso));

		for(Aresta e: g.arestas) {
			Vertice u = e.origem;
			Vertice v = e.destino;
			if(findSet(u) != findSet(v)) {
				A.addArestaNo(A.vertices.get(u.nome), A.vertices.get(v.nome));
				union(u, v);
			}
		}

		return A;
	}

	/*
	public Vertice extractMin(List Q) {
		Vertice min = Q.stream().min(Comparator.comparing(Vertice::))
	}

	public decrease-key(List Q, Vertice v) {

	}

	*/

	/*
	 Random-Tree-Prim(n)
	*/
	public Grafo randomTreePrim(int n) {
		Grafo g = new Grafo();
		Random gerador = new Random();

		Double l[][] = new Double[n][n];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				l[i][j] = gerador.nextDouble();
			}
		}
		g.lista_adj = l;

		for(Integer i = 0; i < n; i++) {
			g.addVertice(i);
		}

		Vertice s = g.vertices.get(gerador.nextInt(n));

		mstPrim(g, l, s);

		return g;
	}

	public void mstPrim(Grafo g, Double[][] w, Vertice s) {
		
		for(Vertice u: g.vertices) {
			u.chave = Double.POSITIVE_INFINITY;
			u.pai = null;
		}
		s.chave = 0;
		List<Vertice> Q = g.vertices;

		while(Q.size() != 0) {
			Vertice u = extractMin(Q);
			for(Vertice v: u.adj) {
				if(Q.contains(v) && w[u.nome][v.nome] < v.chave) {
					v.pai = u;
					v.chave = w[u.nome][v.nome];
					//decrease-key(Q, v);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Grafo g = new Grafo();
		
		g.testeUnitario1();
		g.testeUnitario2();
		
		//g.testeIsTree();
		g.testeRandom();
		
		g.testeUnionFind();

	}

	/* 
	 * teste unitário 1 
	 * java -ea Grafo.java 	->	 ativar o assert e emitir erros se houver algum 
	*/
	public void testeUnitario1(){
		
		/*
		*---*     *---*     *---*
		| 1 |---->| 2 |     | 3 |
		*---*  /> *---*     *---*
		  |   /     |      /  |  
		  v  /      v     /   v  
		*---*     *---*  /  *---*
		| 4 |<----| 5 |</   | 6 |
		*---*     *---*     *---*
		*/

		Grafo g = new Grafo();
		Vertice r = g.addVertice(1);
		Vertice s = g.addVertice(2);
		Vertice t = g.addVertice(3);
		Vertice v = g.addVertice(4);
		Vertice w = g.addVertice(5);
		Vertice x = g.addVertice(6);
		g.addAresta(r, s);
		g.addAresta(r, v);
		g.addAresta(w, v);
		g.addAresta(v, s);
		g.addAresta(s, w);
		g.addAresta(t, w);
		g.addAresta(t, x);

		g.Bfs(g, s);

		assert(s.d == 0.0) : "Erro na distancia do vértice s";
		assert(r.d == Double.POSITIVE_INFINITY) : "Erro na distancia do vértice r";
		assert(v.d == 2.0) : "Erro na distancia do vértice v";
		assert(w.d == 1.0) : "Erro na distancia do vértice w";
		assert(t.d == Double.POSITIVE_INFINITY) : "Erro na distancia do vértice t";
		assert(x.d == Double.POSITIVE_INFINITY) : "Erro na distancia do vértice x";

		assert(maior(g).d == 2.0) : "Erro na função maior, Bfs(g, s)";

		g.Bfs(g,w);
		assert(maior(g).d == 2.0) : "Erro na função maior, Bfs(g, w)";

		g.Bfs(g,t);
		assert(maior(g).d == 3.0) : "Erro na função maior, Bfs(g, t)";

		g.Bfs(g,x);
		assert(maior(g).d == 0.0) : "Erro na função maior, Bfs(g, x)";

		g.Bfs(g,v);
		assert(maior(g).d == 2.0) : "Erro na função maior, Bfs(g, v)";
		
		assert(g.diametro(g, s) == 2.0) : "Erro no diametro 1";
		assert(g.diametro(g, w) == 2.0) : "Erro no diametro 2";
		assert(g.diametro(g, t) == 2.0) : "Erro no diametro 3";
		assert(g.diametro(g, x) == 0.0) : "Erro no diametro 4";
		assert(g.diametro(g, v) == 2.0) : "Erro no diametro 5";

	}

	/* teste unitário 2 */
	public void testeUnitario2(){

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
		Vertice v1 = h.addVertice(1);	
		Vertice v2 = h.addVertice(2);
		Vertice v3 = h.addVertice(3);
		Vertice v4 = h.addVertice(4);
		Vertice v5 = h.addVertice(5);
		h.addAresta(v1, v2);
		h.addAresta(v1, v5);
		h.addAresta(v2, v1);
		h.addAresta(v2, v3);
		h.addAresta(v2, v4);
		h.addAresta(v2, v5);
		h.addAresta(v3, v2);
		h.addAresta(v3, v4);
		h.addAresta(v4, v2);
		h.addAresta(v4, v3);
		h.addAresta(v4, v5);
		h.addAresta(v5, v1);
		h.addAresta(v5, v2);
		h.addAresta(v5, v4);

		h.Bfs(h, v1);

		assert(v1.d == 0.0) : "Erro na distancia do vértice 1";
		assert(v2.d == 1.0) : "Erro na distancia do vértice 2";
		assert(v3.d == 2.0) : "Erro na distancia do vértice 3";
		assert(v4.d == 2.0) : "Erro na distancia do vértice 4";
		assert(v5.d == 1.0) : "Erro na distancia do vértice 5";

		assert(maior(h).d == 2.0) : "Erro na função maior, Bfs(h, v1)";

		h.Bfs(h, v2);
		assert(maior(h).d == 1.0) : "Erro na função maior, Bfs(h, v2)";

		h.Bfs(h, v3);
		assert(maior(h).d == 2.0) : "Erro na função maior, Bfs(h, v3)";

		h.Bfs(h, v4);
		assert(maior(h).d == 2.0) : "Erro na função maior, Bfs(h, v4)";

		h.Bfs(h, v5);
		assert(maior(h).d == 2.0) : "Erro na função maior, Bfs(h, v5)";

		assert(h.diametro(h, v1) == 2.0) : "Erro no diametro 6";
		assert(h.diametro(h, v2) == 2.0) : "Erro no diametro 7";
		assert(h.diametro(h, v3) == 2.0) : "Erro no diametro 8";
		assert(h.diametro(h, v4) == 2.0) : "Erro no diametro 9";
		assert(h.diametro(h, v5) == 2.0) : "Erro no diametro 10";

	}

	/* 
	 * testes para verificar se a função randomTreeRandomWalk gera realmente árvores
	 * utiliza a função isTree que retorna true se o grafo de entrada é uma árvore
	*/
	public void testeIsTree() {
		assert(isTree(randomTreeRandomWalk(250)) == true) : "Grafo 1 não é uma árvore";
		assert(isTree(randomTreeRandomWalk(500)) == true) : "Grafo 2 não é uma árvore";
		assert(isTree(randomTreeRandomWalk(750)) == true) : "Grafo 3 não é uma árvore";
		assert(isTree(randomTreeRandomWalk(1000)) == true) : "Grafo 4 não é uma árvore";
		assert(isTree(randomTreeRandomWalk(1250)) == true) : "Grafo 5 não é uma árvore";
		assert(isTree(randomTreeRandomWalk(1500)) == true) : "Grafo 6 não é uma árvore";
		assert(isTree(randomTreeRandomWalk(1750)) == true) : "Grafo 7 não é uma árvore";
		assert(isTree(randomTreeRandomWalk(2000)) == true) : "Grafo 8 não é uma árvore";

		assert(isTree(randomTreeKruskal(250)) == true) : "Grafo 1 não é uma árvore";
		assert(isTree(randomTreeKruskal(500)) == true) : "Grafo 2 não é uma árvore";
		assert(isTree(randomTreeKruskal(750)) == true) : "Grafo 3 não é uma árvore";
		assert(isTree(randomTreeKruskal(1000)) == true) : "Grafo 4 não é uma árvore";
		assert(isTree(randomTreeKruskal(1250)) == true) : "Grafo 5 não é uma árvore";
		assert(isTree(randomTreeKruskal(1500)) == true) : "Grafo 6 não é uma árvore";
		assert(isTree(randomTreeKruskal(1750)) == true) : "Grafo 7 não é uma árvore";
		assert(isTree(randomTreeKruskal(2000)) == true) : "Grafo 8 não é uma árvore";
	}

	/*
	 * Testes para a estrutura union-find
	 * utiliza alguns vértices e verifica se os métodos retornam o resultado esperado
	 * para o método makeset o esperado é que os vértices tenham como pai o próprio vértice e o rank igual o zero
	 * podemos usar o método findSet para verificar qual é o pai de um vértice x
	 * já para verificar a corretude do método union, nos certificamos que o rank e o pai de um vértice foi alterado corretamente.
	*/
	public void testeUnionFind() {
		Vertice a = new Vertice(1);
		Vertice b = new Vertice(2);
		Vertice c = new Vertice(3);
		Vertice d = new Vertice(4);

		makeSet(a);
		makeSet(b);
		makeSet(c);
		makeSet(d);

		assert findSet(a) == a;
		assert findSet(b) == b;
		assert findSet(c) == c;
		assert findSet(d) != a;

		union(a, b);
		union(b, d);

		assert b.rank == 1;
		assert d.rank == 0;
		assert a.pai.nome == 2;
		assert b.pai.nome == 2;
		assert d.pai.nome != 4;

		assert findSet(a) == findSet(b);
		assert findSet(d) != findSet(c);

	}

	/* 
	 * testes automatizados para verificar se randomTreeRandomWalk realmente gera árvores aleatórias;
	 * testes automatizados para verificar se randomTreeKruskal gera árvores aleatórias com peso mínimo;
	 * gera um arquivo 'diametros.txt' com o valor medio de diametro das árvores resultantes do 
	   algoritmo randomTreeRandomWalk e randomTreeKruskal, que tem como entrada valores predefinidos 
	   para n.
	*/
	public void testeRandom() throws IOException {

		int[] tamanho = {250, 500, 750, 1000, 1250, 1500, 1750, 2000};
		Double soma = 0.0;
		Double media = 0.0;

		FileWriter arq = new FileWriter("./diametros.txt");
    	PrintWriter gravarArq = new PrintWriter(arq);

		Random verticeAleatorio = new Random();
		
		System.out.printf("Random tree Random walk\n");
		gravarArq.printf("Random tree Random walk\n");
		for(int n : tamanho) {
			for(int i = 0; i < 500; i++){
				Grafo g = new Grafo();
				g = randomTreeRandomWalk(n);
				if(isTree(g)) {
					Vertice s = g.vertices.get(verticeAleatorio.nextInt(n));
					soma += g.diametro(g, s);
				}
			}
			media = soma/500;
			soma = 0.0;

			gravarArq.printf("\n%d %.3f", n, media);
		}

		System.out.printf("Random tree Kruskal\n");
		gravarArq.printf("\nRandom tree Kruskal");
		for(int n : tamanho) {
			System.out.printf(n + "\n");
			for(int i = 0; i < 500; i++){
				Grafo g = new Grafo();
				g = randomTreeKruskal(n);
				if(isTree(g)) {
					Vertice s = g.vertices.get(verticeAleatorio.nextInt(n));
					soma += g.diametro(g, s);
				}
			}
			media = soma/500;
			soma = 0.0;

			System.out.printf(media+"\n");
			gravarArq.printf("\n%d %.3f", n, media);
		}
		
		arq.close();

	}

}
