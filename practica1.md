# Práctica 1

En esta práctica diseñaremos e implementaremos la clase `AnalizadorBasico`. Esta clase incluirá una serie de métodos que nos permitirán analizar una colección de tableros de ajedrez. Tal y como se ve en el laboratorio 1, estas partidas se modelan con las clases `Tablero` y `Partida`.

Esta clase se compone de unos métodos obligatorios y otros opcionales. En todos los casos debemos intentar obtener implementaciones eficientes, es decir, que tengan una complejidad temporal baja. Para ello, se recomienda hacer uso de las estructuras de datos adecuadas y de los algoritmos de ordenación y búsqueda que se han visto en la asignatura.

**Nota**: Java cuenta con una clase `Collections` que tiene métodos para ordenar listas y hacer búsquedas en ellas. Sin embargo, es recomendable que en este punto del curso se implemente algún método de ordenación manualmente para entender bien cómo funciona. Las transparencias de la asignatura tienen ejemplos de cómo implementar los diferentes algoritmos de ordenación que se han explicado en la asignatura.

## Paso 1: Crear la clase y su constructor

Empezaremos la práctica creando la clase `AnalizadorBasico` en el paquete `es.upm.dit.adsw.ajedrez25.analizadores`. El constructor de la clase debe recibir como parámetro una lista de partidas que podemos obtener con `LectorPartidas`. En este constructor se deben inicializar los atributos de la clase, y estos deben incluir una lista de partidas y otra lista de tableros que contenga todos los turnos vistos en la colección de partidas. Puede utilizar el siguiente método main para probar el funcionamiento de la clase (hay que añadir los imports necesarios y crear el logger):

```java
public static void main(String[] args) throws Exception {
    LectorPartidas lector = new LectorPartidas("data/partidas.txt");
    long t = System.currentTimeMillis();
    AnalizadorBasico basico = new AnalizadorBasico(lector.getPartidas());
    LOGGER.info("Tiempo de análisis: " + (System.currentTimeMillis() - t) + " ms");
    LOGGER.info("Número de turnos de la partida más corta: " + basico.getNTurnosPartidaMasCorta());
    LOGGER.info("Puntuación mediana: " + basico.getPuntuacionMediana());
    LOGGER.info("Partidas ganadas por mahdii: " + basico.getPartidasGanadasPor("mahdii"));
    LOGGER.info("Mejor jugador: " + basico.getMejorJugador() + " con " + basico.getPartidasGanadasPor(basico.getMejorJugador()) + " victorias");
}
```

## Paso 2: Ordenar tableros

Para conseguir una implementación eficiente en gran parte de los métodos de la clase, es recomendable ordenar la lista de tableros. Para ello, se propone implementar un método `ordenarTableros` que ordene la lista de tableros. Este método debe aplicar alguno de los algoritmos de ordenación que se han visto en la asignatura, idealmente uno que tenga complejidad $O(n log (n))$.

El criterio de ordenación debe basarse en la puntuación general del tablero (diferencia entre la puntuación de las blancas y la de las negras), tal como se definió en pasos anteriores. En caso de empate en la puntuación, se debe mantener el orden relativo original de los tableros en la lista (orden estable).

### Pruebas recomendadas

- Comprobar que la siguiente lista de tableros se ordena correctamente:

Lista original

```text
["....r...p....k...p.....p.......r..P.p.....Pp....P.....bK.....q..",  # -24
 "....rk.......pppp........r..b...............K..n........q.......",  # -29
 "r.b.k..r........p.N..bpp...P.p......pq..P.N.R..P.PP..PP.R..Q..K.",  # +2
 "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR",  # 0
 ".nbqk.nrppppppbp......p..................PN..N..P.PPPPPPR.BQKB.R"]  # +5
```

Lista ordenada

```text
["....rk.......pppp........r..b...............K..n........q.......",  # -29
 "....r...p....k...p.....p.......r..P.p.....Pp....P.....bK.....q..",  # -24
 "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR",  # 0
 "r.b.k..r........p.N..bpp...P.p......pq..P.N.R..P.PP..PP.R..Q..K.",  # +2
 ".nbqk.nrppppppbp......p..................PN..N..P.PPPPPPR.BQKB.R"]  # +5
```

- Comprobar que al ordenar se mantiene el orden de los tableros con la misma puntuación. Es decir, si dos tableros tienen la misma puntuación, el orden en el que aparecen en la lista no debe cambiar.

Lista original

```text
[".nbqk.nrppppppbp......p..................PN..N..P.PPPPPPR.BQKB.R",  # +5
 ".nbqk.nr.pppppbpp.....p..................PN..N..P.PPPPPPR.BQKB.R",  # +5
 "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR"]  # 0
```

Lista ordenada

```text
["rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR",  # 0
 ".nbqk.nrppppppbp......p..................PN..N..P.PPPPPPR.BQKB.R",  # +5
 ".nbqk.nr.pppppbpp.....p..................PN..N..P.PPPPPPR.BQKB.R"]  # +5
```

## Paso 3: Búsqueda de tableros por puntuación

Varios de los análisis que vamos a realizar se basan en encontrar tableros con unas puntuaciones determinadas. Para facilitar esto, se propone implementar un método `buscarTablerosPorPuntuacion` que reciba como parámetro una puntuación y devuelva el índice del primer tablero con esa puntuación en la lista de tableros. En caso de no encontrar ningún tablero con esa puntuación, el método debe devolver la posición en la que debería insertarse el tablero para mantener el orden correcto de la lista. Como se ha visto en la asignatura, esta posición se puede obtener con una búsqueda binaria.

Una posible cabecera de este método podría ser la siguiente, puedes consultar las diapositivas de la asignatura para ver cómo implementar una búsqueda binaria:

```java
public int buscarTablerosPorPuntuacion(int puntuacion) {
    // búsqueda binaria
    return inicio;
}
```

### Pruebas recomendadas

Para verificar que la implementación del método `buscarTablerosPorPuntuacion` funciona correctamente, se recomienda realizar las siguientes pruebas:

1. Caso base: Lista vacía

Si la lista de tableros está vacía, el método debe devolver 0 (la posición donde se insertaría el primer tablero).

2. Buscar una puntuación existente

Dado un conjunto de tableros con puntuaciones ordenadas, buscar una puntuación que exista en la lista debe devolver el índice correcto. Por ejemplo, el tablero con puntuación 0 debería estar en la posición 2 de la lista.
Lista de ejemplo:

```text
[
 "....rk.......pppp........r..b...............K..n........q.......",  // -29
 "....r...p....k...p.....p.......r..P.p.....Pp....P.....bK.....q..",  // -24
 "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR",  // 0
 "r.b.k..r........p.N..bpp...P.p......pq..P.N.R..P.PP..PP.R..Q..K.",  // +2
 ".nbqk.nrppppppbp......p..................PN..N..P.PPPPPPR.BQKB.R",  // +5
 ".nbqk.nr.pppppbpp.....p..................PN..N..P.PPPPPPR.BQKB.R"]  // +5
```

3. Buscar una puntuación inexistente (debe devolver la posición de inserción)

Si la puntuación no está en la lista, el método debe devolver el índice donde debería insertarse para mantener el orden. Por ejemplo, si buscamos el tablero con puntuación 3 (en la lista anterior), debería devolver la posición 4.

4. Buscar el menor y mayor valor de la lista

Si la puntuación buscada es menor que la mínima en la lista, debe devolver 0.
Si la puntuación es mayor que la máxima, debe devolver n (donde n es el tamaño de la lista).

5. Buscar puntuaciones duplicadas

Si hay varios tableros con la misma puntuación, el método debe devolver la posición del primero de ellos. Por ejemplo, si buscamos el tablero con puntuación 5, debería devolver la posición 4.

## Paso 4: Creación de diccionarios

Este paso puede completarse una vez se haya cubierto el tema de diccionarios en la asignatura, si aún no se ha visto, se puede saltar este paso y volver a él una vez se haya cubierto el tema.

Estos diccionarios nos pueden ayudar a conseguir las complejidades pedidas en los métodos de la práctica. Podemos crear los que consideremos necesarios. Uno que nos será de utilidad es uno en el que vinculemos el nombre de un jugador con las partidas que ha ganado. Para ello, podemos hacer algo similar a lo siguiente en el constructor de la clase:

```java
...
this.jugadoresPartida = new HashMap<>();
        for (Partida partida : partidas) {
        if (jugadoresPartida.containsKey(partida.getJugadorBlancas())) {
            jugadoresPartida.get(partida.getJugadorBlancas()).add(partida);
        } else {
            List<Partida> partidasJugador = new ArrayList<>();
            partidasJugador.add(partida);
            jugadoresPartida.put(partida.getJugadorBlancas(), partidasJugador);
        }
    }
...
```

Para que el diccionario funcione correctamente, deberemos añadir el método `hashCode` a la clase Pieza. Podemos usar el siguiente código para ello:

```java
@Override
public int hashCode() {
    return Objects.hash(this.tipo, this.bando);
}
```

## Paso 5: Creación de pruebas

Deberemos crear una clase de pruebas, en la carpeta de test, en el paquete `es.upm.dit.adsw.ajedrez25.analizadores` (hay que crear también el paquete analizadores) que contenga pruebas para los métodos implementados en los pasos anteriores y en los siguientes. De momento, solo se deben implementar pruebas para los métodos `ordenarTableros` y `buscarTablerosPorPuntuacion`.

---

## Paso 6: Métodos obligatorios

## **Resumen de métodos obligatorios y sus complejidades mínimas**

| Método                                  | Complejidad (caso medio)  | Descripción |
|-----------------------------------------|-------------|-------------|
| `getMayorTablero()`                     | $O(1)$     | Devuelve el tablero con la mayor puntuación. |
| `getPuntuacionMediana()`                | $O(1)$     | Devuelve la puntuación mediana de los tableros almacenados. |
| `getNTurnosPartidaMasCorta()`           | $O(p)$     | Devuelve el número de turnos de la partida más corta. |
| `getPartidasGanadasPor(String jugador)` | $O(\frac{p}{m})$     | Devuelve el número de partidas ganadas por un jugador específico. |
| `getMejorJugador()`                     | $O(\frac{p^2}{m})$     | Devuelve el nombre del jugador con más partidas ganadas. |
| `getTableroConPuntuacion(int puntuacion)` | $O(\log n)$ | Busca un tablero con una puntuación específica utilizando búsqueda binaria. |
| `getRepeticionesTableros()`             | $O(1)$     | Devuelve un mapa con la cantidad de veces que se repite cada tablero en la lista. |

Siendo \(n\) el número de tableros almacenados en la lista, \(p\) el número de partidas jugadas y \(m\) el número de jugadores distintos.

### **6.1 Método `getMayorTablero`**

Este método devuelve el tablero con la mayor puntuación en la lista de tableros. Como la lista ya está ordenada, el tablero con mayor puntuación será el último de la lista.

**Pruebas recomendadas:**

- Una lista vacía, donde el método debe devolver `null`.
- Una lista con un solo tablero, donde el método debe devolver ese único tablero.
- Una lista con varios tableros ordenados, donde el método debe devolver el último tablero de la lista.
- Utilizando `partidas.txt`, debe obtener un tablero con puntuación 56.

### **6.2 Método `getPuntuacionMediana`**

Este método devuelve la puntuación mediana de los tableros almacenados. Como la lista está ordenada, la mediana será el valor del tablero en la posición central.

**Pruebas recomendadas:**

- Si la lista está vacía, el método devuelve `0`.
- Si la lista tiene un número impar de tableros, el método devuelve la puntuación del tablero en la posición central.
- Si la lista tiene un número par de tableros, se puede definir la mediana como la puntuación media de los elementos centrales.
- Utilizando `partidas.txt`, debe obtener una puntuación mediana 0.

### **6.3 Método `getNTurnosPartidaMasCorta`**

Este método devuelve el número de turnos de la partida más corta registrada en la lista de partidas.

**Pruebas recomendadas:**  

Se debe verificar que:

- Si no hay partidas, el método devuelve `0`.
- Si hay una sola partida, el método devuelve el número de turnos de esa partida.
- Utilizando `partidas.txt`, debe devolver 6 turnos.

### **6.4 Método `getPartidasGanadasPor(String jugador)`**

Este método devuelve el número de partidas que ha ganado un jugador específico.

**Pruebas recomendadas:**  
Se debe comprobar que:

- Si no hay partidas, el método devuelve `0`.
- Si el jugador no ha jugado ninguna partida, el método devuelve `0`.
- Utilizando `partidas.txt`, el jugador `TrialB` debe tener 57 victorias.

### **6.5 Método `getMejorJugador()`**

Este método devuelve el nombre del jugador con más partidas ganadas.

**Pruebas recomendadas:**  
Se debe verificar que:

- Si no hay partidas, el método devuelve `null` o un valor adecuado.
- Si hay un único jugador con victorias, el método devuelve su nombre.
- Utilizando `partidas.txt`, el jugador con más victorias debe ser `Mephostophilis`.

### **6.6 Método `getTableroConPuntuacion(int puntuacion)`**

Este método busca un tablero con una puntuación específica utilizando búsqueda binaria.

**Pruebas recomendadas:**  
Se debe comprobar que:

- Si la lista está vacía, el método devuelve `null`.
- Si hay un tablero con la puntuación buscada, el método lo devuelve correctamente.
- Si no hay un tablero con la puntuación exacta, el método devuelve `null` o un valor adecuado.

### **6.7 Método `getRepeticionesTableros`**

Este método devuelve un diccionario donde las claves son tableros y los valores son el número de veces que cada tablero aparece en la lista.

**Pruebas recomendadas:**  
Se debe verificar que:

- Si la lista está vacía, el método devuelve un mapa vacío.
- Si hay tableros sin repeticiones, cada uno tiene un valor de `1`.
- Si hay tableros repetidos, el método cuenta correctamente las repeticiones.
- Utilizando `partidas.txt`, el tablero básico debe aparecer 19465 veces (tantas como partidas) y el tablero `rnbqkbnrpp.ppppp..........p........P........P...PPP..PPPRNBQKBNR` debe aparecer 31 veces.

## Paso 6: Métodos opcionales

A continuación se presentan una serie de métodos opcionales que se pueden implementar para mejorar la funcionalidad de la clase `AnalizadorBasico`. Estos métodos no son obligatorios, pero pueden ser útiles para realizar análisis adicionales de las partidas y profundizar en el estudio de la asignatura. Se presentan las cabeceras de los métodos, pero se deja a la elección del estudiante la implementación de los mismos.

```java
/**
 * Obtiene los N tableros que más veces aparecen en todas las partidas analizadas.
 *
 * @param n Número de tableros más frecuentes a obtener.
 * @return Lista con los N tableros más frecuentes.
 */
public List<Tablero> getNTablerosMasFrecuentes(int n);

/**
 * Determina si un jugador ha jugado en una posición específica en alguna de sus partidas.
 *
 * @param jugador Nombre del jugador.
 * @param tablero Tablero a comprobar.
 * @return {@code true} si el jugador ha jugado en esa posición, {@code false} en caso contrario.
 */
public boolean jugadorHaJugadoTablero(String jugador, Tablero tablero);

/**
 * Calcula el número medio de victorias por jugador.
 *
 * @return Un mapa donde la clave es el nombre del jugador y el valor es su número medio de victorias.
 */
public Map<String, Double> getMediaVictoriasPorJugador();

/**
 * Obtiene el número de partidas que han finalizado con una pieza específica presente en el tablero final.
 *
 * @param pieza Pieza a comprobar .
 * @return Número de partidas donde la pieza estaba presente al finalizar.
 */
public int getPartidasFinalizadasConPieza(Pieza pieza);

/**
 * Obtiene el número de tableros que tienen exactamente una puntuación determinada.
 *
 * @param puntuacion Puntuación exacta que se desea buscar.
 * @return Cantidad de tableros con dicha puntuación.
 */
public int getNumeroTablerosConPuntuacion(int puntuacion);

/**
 * Obtiene una lista de los jugadores que nunca han perdido una partida.
 *
 * @return Lista de nombres de jugadores invictos.
 */
public List<String> getJugadoresInvictos();

/**
 * Obtiene el número total de jugadas realizadas en todas las partidas analizadas.
 *
 * @return Número total de movimientos jugados.
 */
public int getTotalJugadasRealizadas();
```
