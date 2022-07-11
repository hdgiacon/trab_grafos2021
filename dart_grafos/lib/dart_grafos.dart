const String BRANCO = "branco";
const String CINZA = "cinza";

class Grafo {
  final vertices = <Vertice>[];
  final arestas = <Aresta>[];
  var listaAdj = <List<double>>[];

  Vertice addVertice(int nome) {
    Vertice v = Vertice(nome);
    vertices.add(v);
    return v;
  }

  void addAresta(Vertice origem, Vertice destino) {
    Aresta e = Aresta(origem, destino);
    origem.addAdj(destino);
    arestas.add(e);
  }

  void addArestaNaoOrientada(Vertice origem, Vertice destino) {
    Aresta e = Aresta(origem, destino);
    origem.addAdj(destino);
    destino.addAdj(origem);
    arestas.add(e);
  }

  void addArestaNaoOrientadaPeso(Vertice origem, Vertice destino, double peso) {
    Aresta e = Aresta.comPeso(origem, destino, peso);
    origem.addAdj(destino);
    destino.addAdj(origem);
    arestas.add(e);
  }

  Vertice? findSet(Vertice? x) {
    if (x!.nome != x.pai!.nome) {
      x.pai = findSet(x.pai);
    }
    return x.pai;
  }

  void makeSet(Vertice x) {
    x.pai = x;
    x.rank = 0;
  }

  void link(Vertice? x, Vertice? y) {
    if (x!.rank! > y!.rank!) {
      y.pai = x;
    } else {
      x.pai = y;
      if (x.rank == y.rank) {
        y.rank = y.rank! + 1;
      }
    }
  }

  void union(Vertice? x, Vertice? y) {
    link(findSet(x), findSet(y));
  }

  void bfs(Grafo g, Vertice s) {
    for (Vertice v in g.vertices) {
      v.d = double.infinity;
      v.pai = Vertice();
      v.cor = BRANCO;
    }

    s.d = 0.0;
    s.pai = Vertice();
    s.cor = CINZA;
    final listQ = <Vertice>[];
    listQ.add(s);

    Vertice u = Vertice();

    while (listQ.isNotEmpty) {
      u = listQ.removeLast();
      for (Vertice v in u.adj!) {
        if (v.cor == BRANCO) {
          v.cor = CINZA;
          v.pai = u;
          v.d = u.d! + 1.0;
          listQ.add(v);
        }
      }
    }
  }

  Vertice maior(Grafo g) {
    Vertice maior = Vertice();
    maior.d = 0.0;

    for (Vertice v in g.vertices) {
      if (v.d != double.infinity) {
        if (v.d! >= double.parse(maior.d.toString())) {
          maior = v;
        }
      }
    }

    return maior;
  }

  double? diametro(Grafo g, Vertice s) {
    Vertice a = Vertice();
    Vertice b = Vertice();

    g.bfs(g, s);
    a = maior(g);

    g.bfs(g, a);
    b = maior(g);

    return b.d;
  }

  Grafo randomTreeRandomWalk(int n) {
    Grafo g = Grafo();
    for (int i = 0; i < n; i++) {
      g.addVertice(i);
    }

    for (Vertice u in g.vertices) {
      u.visitado = false;
    }

    Random verticeAleatorio = new Random();

    Vertice u = g.vertices.removeAt(0);
    u.visitado = true;

    while (g.arestas.length < n - 1) {
      Vertice v = g.vertices.removeAt(verticeAleatorio.nextInt(n));

      if (v.visitado == false) {
        g.addArestaNaoOrientada(u, v);
        v.visitado = true;
      }

      u = v;
    }

    return g;
  }

  bool isConnected(Grafo G) {
    for (Vertice u in G.vertices) {
      if (u.cor == BRANCO) {
        return false;
      }
    }
    return true;
  }

  bool isTree(Grafo G) {
    if (G.arestas.length != G.vertices.length - 1) {
      return false;
    }
    Vertice h;
    h = G.vertices.removeAt(0);
    bfs(G, h);
    return isConnected(G);
  }

  Grafo grafoCompleto(int n) {
    Grafo g = Grafo();
    for (int i = 0; i < n; i++) {
      g.addVertice(i);
    }

    Random gerador = new Random();

    double peso;

    for (int i = 0; i < g.vertices.length; i++) {
      for (int j = i + 1; j < g.vertices.length; j++) {
        peso = gerador.nextDouble();
        g.addArestaNaoOrientadaPeso(g.vertices.removeAt(i), g.vertices.removeAt(j), peso);
      }
    }

    return g;
  }

	Grafo randomTreeKruskal(int n) {
		Grafo g = grafoCompleto(n);

		g = mstKruskal(g);

		return g;
	}

	Grafo mstKruskal(Grafo g) {
		Grafo A = Grafo();

		for(Vertice v in g.vertices) {
			v.pai = A.addVertice(v.nome!);
			makeSet(v);
		}

		// ordenar arestas por ordem crescente de peso (não decrescente)
		g.arestas.sort(Comparator.comparingDouble(Aresta::getPeso));

		for(Aresta e in g.arestas) {
			Vertice u = e.origem;
			Vertice v = e.destino;
			if(findSet(u) != findSet(v)) {
				A.addArestaNaoOrientada(A.vertices[u.nome!], A.vertices[v.nome!]);
				union(u, v);
			}
		}

		return A;
	}

	Vertice extractMin(List<Vertice> Q) {
		return Q.removeAt(0);
	}

	List<Vertice> decreaseKey(List<Vertice> Q) {
		Q.sort(Comparator.comparingDouble(Vertice::getChave));
		return Q;
	}

	Grafo randomTreePrim(int n) {
		Grafo g = Grafo();
		Random gerador = new Random();

		final l = <List<double>>[];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				l[i][j] = gerador.nextDouble();
			}
			l[i][i] = double.infinity;
		}
		g.listaAdj = l;

		for(int i = 0; i < n; i++) {
			g.addVertice(i);
		}

		Vertice s = g.vertices.removeAt(gerador.nextInt(n));

		mstPrim(g, l, s);

		return g;
	}

	void mstPrim(Grafo g, List<List<double>> w, Vertice s) {
		var Q = <Vertice>[];

		for(Vertice u in g.vertices) {
			u.chave = double.infinity;
			u.pai = null;
			Q.add(u);
		}
		s.chave = 0.0;
		Q.sort(Comparator.comparingDouble(Vertice::getChave));

		while(Q.isNotEmpty) {
			Vertice u = extractMin(Q);
			Q.removeAt(0);
			for(int j = 0; j < w.length; j++) {
				Vertice v = g.vertices.removeAt(j);
				if(Q.contains(v) && w[u.nome!][j] < v.chave!) {
					v.pai = u;
					v.chave = w[u.nome!][j];
					Q = decreaseKey(Q);
				}
			}
		}
		for(Vertice v in g.vertices){
			if(v.pai != null) {
				g.addArestaNaoOrientada(v.pai!, v);
			}
		}
	}
}

class Vertice {
  int? nome;
  List<Vertice>? adj;
  double? d;
  Vertice? pai;
  String? cor;
  bool? visitado;
  int? rank;
  double? chave;

  Vertice([this.nome]);

  Vertice.init() {
    nome = -1;
  }

  void addAdj(Vertice v) {
    adj!.add(v);
  }
}

class Aresta {
  Vertice origem;
  Vertice destino;
  double? peso;

  Aresta(this.origem, this.destino);

  Aresta.comPeso(this.origem, this.destino, this.peso);
}
