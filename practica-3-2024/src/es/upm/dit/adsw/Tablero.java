package es.upm.dit.adsw;

/**
 * Esta clase representa un tablero de ajedrez. Contiene información sobre las
 * piezas y el estado del tablero.
 */
public class Tablero implements Comparable<Tablero> {

	private String representacion;
	private boolean mate;

	private Pieza[][] tablero;

	/**
	 * Crea un tablero de ajedrez con las piezas en su posición inicial.
	 * 
	 * @return un objeto Tablero con las piezas en su posición inicial
	 */
	public static Tablero tableroBasico() {
		return new Tablero("rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR");
	}

	/**
	 * Crea un tablero de ajedrez sin piezas.
	 */
	public Tablero() {
		tablero = new Pieza[8][8];
		representacion = "................................................................";
	}

	/**
	 * Crea un tablero de ajedrez con las piezas en la posición especificada.
	 * 
	 * @param tableroSerializado una cadena de 64 caracteres que representa la
	 *                           posición de las piezas en el tablero
	 */
	public Tablero(String tableroSerializado) {
		this.representacion = tableroSerializado;
		tablero = new Pieza[8][8];
		for (int i = 0; i < 64; i++) {
			int fila = i / 8;
			int columna = i % 8;
			char c = tableroSerializado.charAt(i);
			if (c != '.') {
				tablero[fila][columna] = new Pieza(c);
			}
		}
	}

	/**
	 * Devuelve true si el tablero está en mate.
	 * 
	 * @return true si el tablero está en mate
	 */
	public boolean getMate() {
		return mate;
	}

	/**
	 * Marca el tablero como mate o no mate.
	 * 
	 * @param mate true si el tablero está en mate
	 */
	public void setMate(boolean mate) {
		this.mate = mate;
	}

	/**
	 * Devuelve el array bidimensional que representa el tablero.
	 * @return el array bidimensional que representa el tablero
	 */
	public Pieza[][] getTablero() {
		return this.tablero;
	}

	/**
	 * Devuelve una cadena de texto que representa el tablero.
	 * 
	 * @return una cadena que representa el tablero
	 */
	public String toString() {
		return this.representacion;
	}

	/**
     * Calcula la puntuación del tableo para el bando especificado.
     * @param bando el bando para el que se calcula la puntuación
     * @return la puntuación del tablero para el bando especificado
     */
    public int getPuntuacionBando(Bando bando) {
		int puntuacion = 0;
		for (int i = 0; i < 64; i++) {
			int fila = i / 8;
			int columna = i % 8;
			Pieza pieza = tablero[fila][columna];
			if (pieza != null) {
				if (pieza.getBando() == bando) {
					switch (pieza.getTipo()) {
						case REY:
							puntuacion += 100;
							break;
						case REINA:
							puntuacion += 9;
							break;
						case TORRE:
							puntuacion += 5;
							break;
						case ALFIL:
							puntuacion += 3;
							break;
						case CABALLO:
							puntuacion += 3;
							break;
						case PEON:
							puntuacion += 1;
							break;
				} 
			}
		}
		}
		return puntuacion;
	}

	public int getPuntuacion() {
		return getPuntuacionBando(Bando.BLANCAS) - getPuntuacionBando(Bando.NEGRAS);
	}

	/**
	 * Indica si el tablero es igual a otro objeto.
	 * 
	 * @param obj el objeto con el que se compara
	 * @return true si el tablero es igual al objeto especificado; false en caso contrario
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Tablero) {
			Tablero other = (Tablero) obj;
			return this.representacion.equals(other.representacion);
		}
		return false;
	}

	public int primerPeonBlanco() {
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero[i].length; j++) {
				if (tablero[i][j] != null && 
				tablero[i][j].getTipo() == TipoPieza.PEON && 
				tablero[i][j].getBando() == Bando.BLANCAS) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Compara el tablero con otro tablero.
	 * 
	 * @param otro el tablero con el que se compara
	 * @return un número negativo si el tablero es menor que el tablero especificado,
	 *         cero si son iguales, y un número positivo si el tablero es mayor que el
	 *         tablero especificado
	 */
	@Override
	public int compareTo(Tablero otro) {
		if (this.getPuntuacion() == otro.getPuntuacion()) {
			int peonBlanco = this.primerPeonBlanco();
			int peonBlancoOtro = otro.primerPeonBlanco();
			return peonBlanco - peonBlancoOtro;
		}
		return this.getPuntuacion() - otro.getPuntuacion();
	}
}
