package es.upm.dit.adsw.test;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import es.upm.dit.adsw.Bando;
import es.upm.dit.adsw.Tablero;


public class PruebasTablero {
    
    @Test
    public void testTableroBasico() {
        Tablero tablero = Tablero.tableroBasico();
        assertEquals("rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR", tablero.toString());
    }

    @Test
    public void testTablero() {
        Tablero tablero = new Tablero();
        assertEquals("................................................................", tablero.toString());
    }

    @Test
    public void testTableroString() {
        Tablero tablero = new Tablero("rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR");
        assertEquals("rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR", tablero.toString());
    }

    @Test
    public void testGetMate() {
        Tablero tablero = new Tablero();
        Assert.assertFalse(tablero.getMate());
    }

    @Test
    public void testSetMate() {
        Tablero tablero = new Tablero();
        tablero.setMate(true);
        Assert.assertTrue(tablero.getMate());
    }

    @Test
    public void testPuntuacionTableroVacio(){
        Tablero tablero = new Tablero();
        int puntuacionBlanco = tablero.getPuntuacionBando(Bando.BLANCAS);
        int puntuacionNegro = tablero.getPuntuacionBando(Bando.NEGRAS);
        assertEquals(0, puntuacionBlanco);
        assertEquals(0, puntuacionNegro);
    }

    @Test
    public void testPuntuacionTableroInicial(){
        Tablero tablero = Tablero.tableroBasico();
        int puntuacionBlanco = tablero.getPuntuacionBando(Bando.BLANCAS);
        int puntuacionNegro = tablero.getPuntuacionBando(Bando.NEGRAS);
        assertEquals(139, puntuacionBlanco);
        assertEquals(139, puntuacionNegro);
    }

    @Test
    public void testPuntuacion() {
        Tablero tablero = new Tablero("....r...p....k...p.....p.......r..P.p.....Pp....P.....bK.....q..");
        int puntuacionBlanco = tablero.getPuntuacionBando(Bando.BLANCAS);
        int puntuacionNegro = tablero.getPuntuacionBando(Bando.NEGRAS);
        assertEquals(103, puntuacionBlanco);
        assertEquals(127, puntuacionNegro);

    }

    @Test
    public void testEquals() {
        Tablero tablero1 = new Tablero("rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR");
        Tablero tablero2 = new Tablero("rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR");
        Assert.assertTrue(tablero1.equals(tablero2));
    }

    @Test
    public void testCompareTo() {
        Tablero tablero1 = new Tablero("r.b.k..r........p.N..bpp...P.p......pq..P.N.R..P.PP..PP.R..Q..K.");
        Tablero tablero2 = Tablero.tableroBasico();
        Assert.assertTrue(tablero1.compareTo(tablero2) > 0);
    }

    @Test
    public void testCompareTo2() {
        Tablero tablero1 = new Tablero("....rk.......pppp........r..b...............K..n........q.......");
        Tablero tablero2 = Tablero.tableroBasico();
        Assert.assertTrue(tablero1.compareTo(tablero2) < 0);
    }
}
