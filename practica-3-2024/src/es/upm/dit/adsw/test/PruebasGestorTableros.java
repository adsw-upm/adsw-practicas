package es.upm.dit.adsw.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.List;

import es.upm.dit.adsw.LectorPartidas;
import es.upm.dit.adsw.Tablero;
import es.upm.dit.adsw.lab3.GestorTableros;


public class PruebasGestorTableros {

    private static GestorTableros gestor;

    @BeforeClass
    public static void setUp() throws IOException, Exception {
    	System.out.println("Leyendo partidas");
        gestor = new GestorTableros(LectorPartidas.leerPartidas("data/partidas1k.txt"));
    }
    
    @Test
    public void testNumeroPartidas() {
        assertEquals(1000, gestor.getPartidasTablerosConPuntuacionMinima(-1000).size());
    }
    
    @Test
    public void testMayorTablero() {
        Tablero mayor = gestor.mayorTablero();
        assertEquals(30, mayor.getPuntuacion());
    }

    @Test
    public void testGetTablerosConPuntuacionMinima() {
        List<Tablero> tablerosConPuntuacionMinima = gestor.getTablerosConPuntuacionMinima(25);
        assertEquals(24, tablerosConPuntuacionMinima.size());
    }   

    @Test
    public void testGetTablerosConPuntuacionEnRango() {
        List<Tablero> tablerosConPuntuacionEnRango = gestor.getTablerosConPuntuacionEnRango(-5,5);
        assertEquals(54953, tablerosConPuntuacionEnRango.size());
    }

    @Test
    public void testGetPuntuacionMediana() {
        int puntuacionMediana = gestor.getPuntuacionMediana();
        assertEquals(0, puntuacionMediana);
    }
}