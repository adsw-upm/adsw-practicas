package es.upm.dit.adsw.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import es.upm.dit.adsw.LectorPartidas;
import es.upm.dit.adsw.Partida;
import es.upm.dit.adsw.lab3.GraphPartidas;
import es.upm.dit.adsw.p3.BuscadorTableros;
import es.upm.dit.adsw.p3.BuscadorTablerosMonitor;
import es.upm.dit.adsw.p3.InterfazBuscador;

public class PruebasConcurrencia {

	static GraphPartidas g;
	static List<Partida> partidas;
	
	@BeforeClass
	public static void setUp() {
		System.out.println("Cargando grafo de prueba");
		try {
			partidas = LectorPartidas.leerPartidas("data/muestra.txt");
			g = new GraphPartidas(partidas);
		} catch (Exception e) {
			System.out.println("Falló la carga del grafo");
			e.printStackTrace();
		}
	}

	public void smokeTest(InterfazBuscador buscador) throws Exception {
		String origen = "rnbqkbnrpppppppp.............................N..PPPPPPPPRNBQKB.R";
		String destino = "................................................................";
		assertEquals(null, buscador.getDistancia(origen, destino));

		assertEquals(0, buscador.getDistancia(origen, origen).intValue());

		origen = "rnbqkbnrpppppppp....................P...........PPPP.PPPRNBQKBNR";
		destino = "........Q.pk....K.......r.......Pq.......P.P..n...P.n...........";
		assertEquals(89, buscador.getDistancia(origen, destino).intValue());

	}
	
	@Test
	public void testBuscadorTableros() throws Exception {
		InterfazBuscador buscador = new BuscadorTableros(g);
		smokeTest(buscador);

	}
	
//	@Test
//	public void testBuscadorTablerosMonitor() throws Exception {
//		InterfazBuscador buscador = new BuscadorTablerosMonitor(g);
//		smokeTest(buscador);
//	}

}
