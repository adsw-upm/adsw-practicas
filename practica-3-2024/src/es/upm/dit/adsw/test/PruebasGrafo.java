package es.upm.dit.adsw.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import es.upm.dit.adsw.LectorPartidas;
import es.upm.dit.adsw.Partida;
import es.upm.dit.adsw.lab3.GraphPartidas;

public class PruebasGrafo {
    private static GraphPartidas grafo;

    @BeforeClass
    public static void setUp() {
        try {
            List<Partida> partidas = LectorPartidas.leerPartidas("data/muestra.txt");
            grafo = new GraphPartidas(partidas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTamanoGrafo() {
        assertEquals(235, grafo.getNodes().size());
        assertEquals(234, grafo.getLinks().size());
    }

    @Test
    public void testGetNode() {
        assertNotNull(grafo.getTableroToNodeMap().get("........Q.pk....K.......r.......Pq.......P.P..n...P.n..........."));
    }

    @Test
    public void testBFS() {
        String origen = "rnbqkbnrpppppppp.............................N..PPPPPPPPRNBQKB.R";
        String destino = "................................................................";
        assertEquals(-1, grafo.BFS(origen, destino));
    }

    @Test
    public void testBFS2() {
        String origen = "rnbqkbnrpppppppp.............................N..PPPPPPPPRNBQKB.R";
        String destino = "rnbqkbnrpppppppp.............................N..PPPPPPPPRNBQKB.R";
        assertEquals(0, grafo.BFS(origen, destino));
    }

    @Test
    public void testBFS3() {
        String origen = "rnbqkbnrpppppppp....................P...........PPPP.PPPRNBQKBNR";
        String destino = "........Q.pk....K.......r.......Pq.......P.P..n...P.n...........";
        assertEquals(89, grafo.BFS(origen, destino));
    }
}
