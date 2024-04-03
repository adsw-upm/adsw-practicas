# Práctica 2

Esta práctica es una continuación directa del Laboratorio 2 y se utilizará el mismo proyecto. 
En los últimos pasos se implementaban formas de recuperar la partida de un tablero mediante búsquedas lineales. 
Las pruebas finales permitian ver que los tiempos de ejecución de esas soluciones para el conjunto de
partidas y tableros que tenemos en el fichero partidas.txt, tienen un tiempo largo para la búsqueda lineal. En estas
prácticas vamos a emplear soluciones alternativas. En concreto vamos a hacer pruebas con dos
tipos de diccionarios que soporta la biblioteca Java:

- HashMap, esta es una implementación del interfaz Map empleando tablas hash.
- TreeMap, esta es una implementación del interfaz Map empleando árboles binarios.

Es importante que declaremos las variables y parámetros con el tipo Map, y de esa forma los diccionarios que vamos
a utilizar solo tendremos que cambiarlos en la construcción. El resto de operaciones se basan en el interfaz Map.

Vamos a comprobar el tiempo que empleamos en localizar las partidas de los tableros y por eso empezaremos guardando 
el tiempo que ha tardado en ejecutarse la prueba del último paso del laboratorio, así como el máximo número de tableros
(la variable `maximo`de la prueba).

```
        int maximo=100000;
        long t=System.nanoTime();
        terminar:
        for (Partida pp : gestor.partidas)
        	for (Tablero tt : pp.turnos) {
	        	if (!pp.url.equals(gestor.partidaDeTablero(tt).url))
	        		System.out.println(pp.url+" "+gestor.partidaDeTablero(tt).url);
	        	if (maximo-- < 0)
	        		break terminar;
        	}
        System.out.println(System.nanoTime()-t);
 ```

Los métodos con los que trabajamos en el paso 2 del laboratorio fueron:

- `public Partida partidaDeTablero(Tablero t)` : Devuelve la partida en la que está incluido un cierto tablero. Si ese 
tablero no está en ninguna partida, devuelve null.
- `public Partida partidaMayorTablero()` : Devuelve la partida del tablero con mayor puntuación general. Este 
método puede reutilizar los métodos `mayorTablero` y `partidaDeTablero` en su implementación.
- `public List<Partida> getPartidasTablerosConPuntuacionMinima(int puntuacion)` : Devuelve una lista con las 
partidas que tienen tableros con una puntuación igual o superior a la puntuación mínima especificada. Este 
método puede reutilizar los métodos `getTablerosConPuntuaconMinima` y `partidaDeTablero` en su implementación.

En esta práctica sólo trabajaremos con esos métodos de `GestorTableros` y actualizando los métodos `equals`, 
`hashCode` y `compareTo` de `Tablero`.

## Paso 1: Obtener la partida de un tablero, basado en tablas hash

El objetivo es modificar el método partidaDeTablero utilizando tablas hash. La implementación de partidaDeTablero, partidaMayorTablero y getTablerosConPuntuaconMinima no se pueden basar en busqueda binaria. 

Es importante tener clara la forma en la que trabaja cada implementación de `Map`.

En la clase `GestorTableros`, el atributo que relacionen tablero y partida se debe se basar en el intefaz Map, como se muestra:

```
private Map<Tablero, Partida> tablero2Partida;
```

Podemos incluir un getter (este getter nos devuelve `tablero2Partida`que es privado), que podremos utilizar 
en algunas pruebas JUnit. Debemos inicializar ese `Map` en el constructor de `GestorTableros`. Empezaremos 
utilizando un `HashMap`, y debemos hacer puts en el `Map` para todos los tableros, utilizando como valores, 
las partidas de los tableros. Este diccionario nos devolvera la partida para cada uno de los tableros. Esta 
inicialización del mapa con sus puts, la podemos hacer en el constructor de `GestorTableros`.

Empezaremos construyendo una prueba que compruebe que el tamaño del `keySet` del mapa, coincide con el 
tamaño de la lista `tableros` que utilizamos en el laboratorio y en prácticas anteriores. Es importante
incluir esta prueba, porque veremos que en algunos casos nos falla.

En el laboratorio 2 implementamos el método `partidaDeTablero` con búsquedas lineales. Hay que hacer una 
nueva implementación empleando el mapa, como se ha indicado previaente. Si el resto de los métodos estaban implementados en función
de este método, podemos seguir empleando las pruebas desarrolladas en el laboratorio.

Se trata de comparar el tiempo de ejecución entre estas dos implementaciones de `partidaDeTablero` para procesar todos los tableros.
Veremos que los tiempos de ejecución serán mucho menores, y podemos ejecutar la prueba para todos los tableros (podemos poner
`maximo` a 800000 y ejecutaremos para todos los tableros).

La funcionalidad de `equals` y `hashCode` es muy importante en el comportamiento del método desarrollado. Si no son adecuados, las funciones del diccionaro no sería válido. La clase `HashMap` compara las claves por valor utilizando una combinación de `hashCode` e `equals` para decidir cuando  dos claves son iguales. `HashMap` trata dos claves como iguales cualdo la comparación de `equals`devuelve `true` y las 
dos claves devuelven el mismo valor de `hashCode`. El método `equals` de la clase `Tablero` es la siguiente (salvo que lo hayamos cambiado):

```
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
```

Que devuelve `true` si dos tableros tienen las mismas fichas, aunque pertenezca a partidas diferentes.
La documentación de `equals` en la clase `Object` nos dice los siguiente:

```
Note that it is generally necessary to override the hashCode method whenever this method is overridden,
so as to maintain the general contract for the hashCode method, which states that equal objects must
have equal hash codes.
```

Si no redefinimos `hashCode`, puede ser que la implementación por defecto de `hashCode` de dos objetos
devuelva el mismo valor, o puede que devuelvan valores diferentes. Y puede ser que `HashMap` cuando dos objetos
que `equals` devuelve `true`, unas veces nos trata las claves como iguales y otras veces no, según si la implementación 
por defecto de `hashCode` devuelve valores iguales o diferentes para esos dos objetos. Este es el motivo por el que debemos
hacer que `hashCode` e `equals` sean consistentes. Este tipo de funcionamientos (unas veces un put nos remplaza el valor 
de una clave, y otras veces no) puede crear errores segùn sea la implementación por defecto de `hashCode`, unas veces 
aparece el error (normalmente muy pocas, porque las implementaciones por defecto de `hashCode` no suele devolver valores iguales
para objetos diferentes) y otras veces no.

Para hacer consistentes `equals` y `hashCode` en `Tablero` podemos reutilizar la función `hashCode` de `String` para el atributo `representacion` (el `equals `de `Tablero`está basado en el `equals` de `String`) incluimos en `Tablero`
el siguiente `hashCode` (para hacer equivalentes `equals` y `hashCode`, y cumplir la consistencia entre `equals` y `hashCode`):

```
	@Override
	public int hashCode() {
		return representacion.hashCode();
	}
```

Si ejecutamos ahora la prueba anterior veremos que el número de claves del mapa no coincide con el número de tableros.
Si queremos que cada tablero tenga asociado su propia partida, dos tableros no se pueden tratar como la misma clave.
Esta prueba  intenta identificar algunos de los problemas que nos podemos encontrar con nuestras implementaciones de `equals`
y `hashCode`. Si ejecutamos la prueba que mide los tiempos de ejecución, veremos que nos identifica los tableros para los
que la coincidencia de `hashCode` e `equals` para dos tableros, hace que el `put` del segundo tablero haya provocado un remplazamiento
del valor del primero.

Si queremos seguir teniendo clave para todos los tableros, debemos hacer que las implementaciones de `hashCode` e `equals`sean
consistentes, pero dos tableros no deben ser tratados como claves iguales. Las implementaciones por defecto de `equals` y `hashCode`
nos lo garantizarían porque por defecto `equals` devlve `true` solo cuando las dos referencias son al mismo objeto. Si borramos los `equals` y `hashCode` de `Tablero` estos problemas no aparecen, pero no podremos utilizar la función `equals` que tenìamos implementada, para 
otras pruebas.

La conclusión fundamental de este paso es que `equals` y `hashCode` deben ser consistentes en las clases, y que según sea su implementación
dos claves que queremos que se comparen por identidad en `HashMap`, sean comparadas en `equals` por identidad y no por valor.

### Pruebas recomendadas

Además de la prueba que comprueba que tenemos claves para todos los tableros en el mapa,
podemos reutilizar las pruebas que tenemos de la segunda parte del laboratorio:

- El constructor de la clase `GestorTableros` deja en un atributo de la clase la lista de las partidas.
Podemos recuperar la primera de las partidas y de la primera de las partidas recuperamos el primero de los turnos y podemos 
comprobar que la partida que nos devuelve `partidaDeTablero` es la primera de las partidas.
- Podemos construir un nuevo tablero que no está en ninguna partida, y comprobamos que `partidaDeTablero` devuelve null 
para ese tablero.


## Paso 2: Obtener la partida de un tablero, basado en árboles binarios

El objetivo es modificar el método `partidaDeTablero` utilizando árboles binarios. La implementación de `partidaDeTablero`, `partidaMayorTablero` y `getTablerosConPuntuaconMinima` no se pueden basar en busqueda binaria. 


En este paso vamos a suponer que la versión que tenemos de `Tablero`es la misma que teniamos al terminar el laboratorio 2.
No tenemos `hashCode` en `Tablero`, `Tablero` implementa `Comparable`, y la implementación de `equals`es:

```
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
```

Una clase Java de la biblioteca que implementa el interfaz `Map` con árboles binarios Red-Black es la
clase `TreeMap`.

Si hemos utilizado declaraciones de variables con tipo `Map<Tablero,Partida>`, simplemente podemos remplazar `new HashMap()`
por `new TreeMap()`. Y ejecutar las pruebas.

Veremos que todo compila, pero las pruebas en general no funcionan. El motivo fundamental es que `TreeMap` y `HashMap` implementan las dos 
`Map`, pero `TreeMap` utiliza fundamentalmente `Comparable::compareTo`, para hacer las comparaciones, y `HashMap` utiliza
fundamentalmente `hashCode` e `equals` en sus comparaciones de claves. Y los métodos `compareTo` y `equals` de `Tablero` no son
consistentes. Las implementaciones que tenemos son las siguientes:

- `compareTo` fija las comparaciones entre tableros en función de las puntuaciones
- `equals` fija las comparaciones en función de la `representacion`

Y ambas formas no tienen por que coincidir. Por ejemplo, dos tableros con la misma puntuación no tiene porque incluir las mismas fichas.
En el fichero partidas.txt tenemos unos cuantos tableros  con representación diferente, pero
puntuaciones iguales. Para los 700.000 tableros de partidas.txt tenemos menos de 100 puntuaciones diferentes, por lo que la mayoría de 
los tableros (si no todos) tienen puntuaciones repetidas, y para las comparaciones entre puntuaciones repetidas, `compareTo` devuelve 0.

La documentación de `Comparable` no obliga, pero recomienda que `compareTo` e `equals` sean consistentes:

```
It is strongly recommended (though not required) that natural orderings be consistent with equals. This is
so because sorted sets  (and sorted maps) without explicit comparators behave "strangely" when they are
used with elements (or keys) whose natural  ordering is inconsistent with equals. In particular, such a
sorted set (or sorted map) violates the general contract for set (or map), which is defined in terms of
the equals method.
```

La documentación de `TreeMap` también aclara este problema.

La implementación de `put` de `TreeMap` compara la clave que queremos incluir, y recorre el árbol binario en función de los
resultados que va devolviendo la comparación entre las claves de los nodos del árbol y la clave que queremos incluir. Si en 
alguna de esas comparaciones `compareTo`devuelve 0, remplaza el valor del nodo con el valor del `put`, y en nuestro caso
perdemos tableros de otras partidas que hubiesemos incluido antes.

La implementación de `get` nos creará problemas parecidos, si intentamos recuperar la partida de un tablero, nos 
puede devolver la partida de otro tablero con la misma puntuación.

Para poder utilizar `TreeMap` debemos cambiar la implementación de `Comparable` de `Tablero`. Si queremos, 
seguir utilizando la implementación de `compareTo` para ordenar por puntuación, pero queremos poder utilizar
todos los tabletos como clave de `TreeMap`, debemos incluir un segundo criterio de comparación, y evitar devolver 
0 cuando comparamos 2 tableros. Podemos cambiar la implementación de `Tablero` para incluir nuevos atributos que 
permitan utilizar ese segundo criterio de comparación cuando las puntuaciones son iguales. Dos ejemplos diferentes de soluciones serían:

- Podemos asignar un identificador de tablero cada vez que construimos un tablero. Un atributo `static` entero nos puede representar
el identificador global del último tablero construido. Antes de asignar identificador al tablero, incrementamos el identificador
global y asignamos un nuevo identificador. El identificador de cada tablero se puede utilizar como segundo criterio
de comparación cuando las puntuaciones son iguales.
- Incluir dentro del tablero la url de la partida a la que pertenece, y su número de turno dentro de la partida.
Podemos utilizar esos dos atributos, para comparar primero por puntuación, y cuando sean de igual puntuación, por url y turno de partida. Pare implementar esta solución debemos incluir los setters de url y turno en `Tablero`y actualizar esos atributos cuando construimos el mapa en el constructor de `GestorTableros`.

Con esas modificaciones evitaremos los problemas que teníamos y podremos hacer una nueva implementación y comparar sus tiempos
de ejecución.

Otra alternativa, si no queremos tocar la implementación de `Tablero`, es construir `TreeMap` pasado como parámetro una instancia 
de `Comparator` (que implementa `compare`) y `TreeMap` utilizará ese comparador en la ordenación de 
las claves; la implementación de `compare` podrá definir el orden que nos interese entre los tableros. 

Veremos que `HashMap` es en general mas rápido, pero `HashMap` ocupa mucha mas memoria, y `TreeMap` mantiene ordenadas
las claves, y podemos recorrer las claves en orden, sin tener que utilizar `sort`, esa ordenación nos permite implementar 
`mayorTablero` y `getTablerosPuntuacionMinima` sin tener que utilizar `sort`. Por otro lado, la construcción del `TreeMap`
es en general lenta.

