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
		
		String nome;
		List<Vertice> adj;
		Double d;
		Vertice pai;
		String cor;
		boolean visitado;

		/* construtor de um vértice padrão */
		Vertice(String nome) {
			this.nome = nome;
			this.adj = new ArrayList<Vertice>();
		}

		/* construtor de um vertice nulo para inicialização */
		Vertice() {
			this.nome = "Nulo";
		}

		/* adiciona um vértice na lista de adjacencia */	
		void addAdj(Vertice v) {
			adj.add(v);
		}

	}

	/* definição de aresta */
	public class Aresta {
		
		Vertice origem;
		Vertice destino;

		/* construtor de uma aresta */
		Aresta(Vertice origem, Vertice destino) {
			this.origem = origem;
			this.destino = destino;
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

	/* Adiciona aresta orientada no grafo */
	public void addAresta(Vertice origem, Vertice destino){
		Aresta e =  new Aresta(origem, destino);
		origem.addAdj(destino);
		arestas.add(e);
	}

	/* Adiciona aresta não orientada no grafo */
	public void addArestaNo(Vertice origem, Vertice destino){
		Aresta e =  new Aresta(origem, destino);
		origem.addAdj(destino);
		destino.addAdj(origem);
		arestas.add(e);
	}
	
	/*
	BFS
		- Calcula a distância de todos os vertíces alcançáveis 
		a partir de um vértice de origem s;
		- Resulta em uma arvore;

		- Bfs(grafo, vertice) => resulta em um efeito colateral no grafo,
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

		Vertice u = new Vertice("u");

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
	função maior	
		- Função que retorna o vértice com maior distância;
		- A entrada precisa ser uma árvore;
		- Utilizado após o bfs para obter o vértice v com maior distancia do vértice u,
		faz parte do cálculo do diametro do grafo;
		- maior(grafo) => maior

		- maior(G) => 3
		- maior(H) => 2
		- maior(I) => 1
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
	função diâmetro
		- comprimento do maior caminho do grafo;
		- entrada precisa ser uma árvore
		- diametro(grafo, vertice) => maior comprimento do grafo.

		- diametro(G, s) => 2
		- diametro(H, v1) => 3
		- diametro(I, a) =>	1
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
	função random tree
	 	- recebe um número inteiro n > 0; 
		- produz uma árvore contendo os n vertices partindo de um vértice aleatório u.

		randomTreeRandomWalk(n) => G
		- onde G é um grafo formado pela árvore.

		- randomTreeRandomWalk(10) => árvore com 10 vértices
		- randomTreeRandomWalk(32) => árvore com 32 vértices
		- randomTreeRandomWalk(100) => árvore com 100 vértices
	*/
	public Grafo randomTreeRandomWalk(int n) {
		Grafo g = new Grafo();
		for(Integer i = 0; i < n; i++) {
			String nomeVertice = i.toString();
			g.addVertice(nomeVertice);
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
	função isConnected
		- verifica se um grafo G possui algum vértice de cor branco;
		- se não houver, então G é conexo(true).

		- isConnected(G) => true
		- isConnected(G) => false
	*/
	boolean isConnected(Grafo G){
		for(Vertice u: G.vertices){
			if(u.cor.equals(BRANCO)){
				return false;
			}
		}
		return true;
	}

	/*
	função isTree
		- verifica se um grafo G é uma árvore;
		- a condição if verifica
		- se não houver ciclos e o grafo for conexo, então G é uma árvore(true).

		- isTree(G) => true
		- isTree(G) => false
	*/
	boolean isTree(Grafo G){
		if(G.arestas.size() != G.vertices.size() - 1){
			return false;
		}  
		Vertice h;
		h = G.vertices.get(0);
		Bfs(G, h);
		return isConnected(G);
	}

	public static void main(String[] args) throws IOException{
		Grafo g = new Grafo();
		
		g.testeUnitario1();
		g.testeUnitario2();
		
		g.testeIsTree();
		g.testeRandom();
	}

	/* 
		- teste unitário 1 
		- java -ea Grafo.java 	->	 ativar o assert e emitir erros se houver algum 
	*/
	public void testeUnitario1(){
		
		/*
		*---*     *---*     *---*
		| R |---->| S |     | T |
		*---*  /> *---*     *---*
		  |   /     |      /  |  
		  v  /      v     /   v  
		*---*     *---*  /  *---*
		| V |<----| W |</   | X |
		*---*     *---*     *---*
		*/

		Grafo g = new Grafo();
		Vertice r = g.addVertice("r");
		Vertice s = g.addVertice("s");
		Vertice t = g.addVertice("t");
		Vertice v = g.addVertice("v");
		Vertice w = g.addVertice("w");
		Vertice x = g.addVertice("x");
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
		Vertice v1 = h.addVertice("1");	
		Vertice v2 = h.addVertice("2");
		Vertice v3 = h.addVertice("3");
		Vertice v4 = h.addVertice("4");
		Vertice v5 = h.addVertice("5");
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

	/* testes para verificar se a função randomTreeRandomWalk gera realmente árvores
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
	}

	/* testes automatizados para verificar se randomTreeRandomWalk realmente gera árvores aleatórias
	 * gera um arquivo 'diametros.txt' com o valor medio de diametro
	 * das árvores resultantes do algoritmo randomTreeRandomWalk, que tem como entrada
	 * valores predefinidos para n.
	*/
	public void testeRandom() throws IOException{

		int[] tamanho = {250, 500, 750, 1000, 1250, 1500, 1750, 2000};
		Double soma = 0.0;
		Double media = 0.0;

		FileWriter arq = new FileWriter("./diametros.txt");
    	PrintWriter gravarArq = new PrintWriter(arq);

		Random verticeAleatorio = new Random();
		
		gravarArq.printf("Random tree\n");
		for(int n : tamanho) {
			for(int i = 0; i < 1000; i++){
				Grafo g = new Grafo();
				g = randomTreeRandomWalk(n);
				Vertice s = g.vertices.get(verticeAleatorio.nextInt(n));
				soma += g.diametro(g, s);
			}
			media = soma/1000;
			soma = 0.0;

			gravarArq.printf("\n%d %.3f", n, media);
		}

		arq.close();

	}

}