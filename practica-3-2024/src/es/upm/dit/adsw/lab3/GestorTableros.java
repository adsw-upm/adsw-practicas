package es.upm.dit.adsw.lab3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.upm.dit.adsw.Partida;
import es.upm.dit.adsw.Tablero;

/**
 * Esta clase representa un buscador de partidas de ajedrez.
 * Contiene métodos para buscar partidas en una lista de objetos Partida según
 * distintos criterios.
 */
public class GestorTableros {

    private List<Tablero> tableros;
    private List<Partida> partidas;
    private Map<Tablero, Partida> tablero2Partida;

    public static void main(String[] args) throws Exception {
        //GestorTableros gestor = new GestorTableros(LectorPartidas.leerPartidasZip("data/partidas_extendido.zip"));
    }

    /**
     * Crea un objeto GestorTableros con una lista de partidas.
     * @param partidas una lista de objetos Partida
     */
    public GestorTableros(List<Partida> partidas) {
        this.tableros = new ArrayList<>();
        this.tablero2Partida = new HashMap<>();
        this.partidas = partidas;
        for (Partida p : partidas) {
        	for (Tablero t : p.turnos) {
        		tablero2Partida.put(t, p);
        		tableros.add(t);
        	}
        }
        Collections.sort(tableros);
    }

    /**
     * Devuelve el tablero con mayor puntuación general.
     * @return el tablero con mayor puntuación general
     */
    public Tablero mayorTablero() {
        return tableros.get(tableros.size() - 1);
    }

    /**
     * Devuelve una lista con los tableros que tienen una puntuación igual o
     * superior a la puntuación mínima especificada.
     * @param puntuacion la puntuación mínima
     * @return una lista con los tableros que tienen una puntuación igual o
     * superior a la puntuación mínima especificada
     */
    public List<Tablero> getTablerosConPuntuacionMinima(int puntuacion) {
        return getTablerosConPuntuacionEnRango(puntuacion, Integer.MAX_VALUE);
    }

    public List<Tablero> getTablerosConPuntuacionEnRango(int puntuacionMinima, int puntacionMaxima) {
        int posMin = binarySearch(puntuacionMinima);
        int posMax = binarySearch(puntacionMaxima);
        while (posMax < tableros.size() && tableros.get(posMax).getPuntuacion() == puntacionMaxima) {
            posMax++;
        }

        return tableros.subList(posMin, posMax);
    }

    /**
        * Encuentra la primera posición en la lista de tableros con una puntuación
        * igual a la puntuación especificada. Si la lista no contiene tableros con
        * esa puntuación, devuelve la posición en la que debería insertarse un
        * tablero con esa puntuación.
        * @param puntuacion la puntuación a buscar
        * @return la primera posición en la lista de tableros con una puntuación
        * igual a la puntuación especificada
        */
    public int binarySearch(int puntuacion) {
        int left = 0;
        int right = tableros.size();
        while (left < right) {
            int mid = (left + right) / 2;
            if (tableros.get(mid).getPuntuacion() < puntuacion) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        if (left == tableros.size()) {
            return left;
        }
        int valueInLeft = tableros.get(left).getPuntuacion();
        while (left>0 && tableros.get(left-1).getPuntuacion() == valueInLeft) {
            left--;
        }
        return left;
    }

    /**
     * Devuelve la puntuación mediana de los tableros.
     * @return la puntuación mediana de los tableros
     */
    public int getPuntuacionMediana() {
        int n = tableros.size();
        if (n % 2 == 0) {
            return (tableros.get(n / 2 - 1).getPuntuacion() + tableros.get(n / 2).getPuntuacion()) / 2;
        } else {
            return tableros.get(n / 2).getPuntuacion();
        }
    }

    public Partida partidaDeTablero(Tablero t) {
        for (Partida p : partidas) {
            for (Tablero tablero : p.turnos) {
                if (tablero.equals(t)) {
                    return p;
                }
            }
        }
        return null;
    }

    public Partida partidaMayorTablero() {
        return partidaDeTablero(mayorTablero());
    }

    public List<Partida> getPartidasTablerosConPuntuacionMinima(int puntuacion) {
        List<Tablero> tablerosConPuntuacionMinima = getTablerosConPuntuacionMinima(puntuacion);
        List<Partida> partidasConPuntuacionMinima = new ArrayList<>();
        for (Tablero tablero : tablerosConPuntuacionMinima) {
            partidasConPuntuacionMinima.add(partidaDeTablero(tablero));
        }
        return partidasConPuntuacionMinima;
    }

}
