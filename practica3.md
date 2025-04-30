# Práctica 3

En esta última práctica, vamos a mejorar el análisis del grafo de partidas de ajedrez. Para ello diseñaremos un sistema concurrente que nos permita paralelizar la búsqueda de caminos en el grafo. Para realizar esta práctica, es necesario haber completado los 3 primeros pasos de la práctica 2.

El objetivo de esta práctica es implementar un sistema que permita la búsqueda paralela de caminos en el grafo. Esto puede ayudarnos a entender el estado de una partida de la misma manera que en la práctica 2. Sin embargo, en esta ocasión intentaremos mejorar el rendimiento de esta exploración mediante el uso de un pool de hebras. Un pool de hebras es un mecanismo de concurrencia que permite manejar un conjunto de hebras que van resolviendo tareas de forma concurrente. 

Aunque existen algunos mecanismos para implementar pools de hebras en Java, como el ExecutorService, en esta práctica implementaremos nuestro propio pool de hebras. Esto nos permitirá entender mejor cómo funcionan y cómo podemos utilizarlos para resolver problemas de forma concurrente. Lo haremos a través de la implementación de una clase llamada PoolHebras, que seguirá los principios de diseño de un monitor, y que se encargará de gestionar un conjunto de hebras y asignarles tareas a medida que estén disponibles.

## Paso 0: Preparación del entorno

En vez de volver a procesar las partidas de ajedrez, vamos a utilizar el grafo que generamos en la práctica 2. Trabajaremos sobre la clase AnalizadorConcurrente.java. A la hora de crear su constructor, lo haremos de la misma manera que en las otras prácticas: recibiendo bien un objeto LectorPartidas o directamente una lista de objetos Partida. En esta ocasión sin embargo no haremos ningún tipo de procesado nuevo, si no que reutilizaremos lo que hicimos en la práctica 2. Tendremos las mismas estructuras de datos que en la clase AnalizadorGrafos, y añadiremos un objeto PoolHebras que inicializaremos con un número fijo de hebras. El constructor debería ser algo así:

```java
public static final int NUM_HEBRAS = 10; // Definimos el número de hebras

public AnalizadorConcurrente(LectorPartidas lectorPartidas) {
        this(lectorPartidas.getPartidas());
    }

public AnalizadorConcurrente(List<Partida> partidas) {
    AnalizadorGrafos analizadorGrafos = new AnalizadorGrafos(partidas);
// Creamos los métodos accesores si no los tenemos ya
    this.nodos = analizadorGrafos.getNodos();
    this.enlaces = analizadorGrafos.getEnlaces();
    this.pool = new PoolHebras(NUM_HEBRAS); 
}
```

En un proyecto real, las clases de un proyecto se suelen definir en archivos separados. En este caso, teniendo en cuenta el propósito didáctico y para mantener todo el código desarrollado para la práctica en un único archivo, vamos a definir las otras clases que utilizaremos en el mismo archivo. Java nos permite hacer esto siempre que las clases que definamos sean internas. Esta solución la utilizamos ya en el laboratorio 0. Necesitaremos definir la clases PoolHebras, HebraWorker, TareaCamino, cuyo funcionamiento veremos en los siguientes puntos. Para hacer esta definición, utilizaremos la siguiente sintaxis:

```java

public class AnalizadorConcurrente {
    // Definición de la clase PoolHebras
    class PoolHebras {
        // ...
    }
    // Definición de la clase HebraWorker
    class HebraWorker extends Thread { // También se puede hacer con implements Runnable
        // ...
    }
    // Definición de la clase TareaCamino
    class TareaCamino {
        public final Nodo nodoAVisitar;
        public final List<Nodo> caminoRecorrido;

        public TareaCamino(Nodo nodoAVisitar, List<Nodo> caminoRecorrido) {
            this.nodoAVisitar = nodoAVisitar;
            this.caminoRecorrido = caminoRecorrido;
        }
    }

}
```

Es decir, colocamos las clases dentro de la clase `AnalizadorConcurrente`. Además de las clases, definiremos un método que nos permita iniciar la búsqueda de caminos desde un tablero que reciba como parámetro. Este método se llamará `buscarCaminos`, y tendrá la siguiente estructura:

```java
public List<List<Nodo>> buscarCaminos(Tablero tablero) {
    // Obtenemos el nodo inicial a partir del tablero
    if (!nodos.containsKey(tablero)) {
        return null; // Si el tablero no está en el grafo, devolvemos null
    }
    Nodo nodoInicial = nodos.get(tablero);
    pool.buscarCaminos(nodoInicial);

    while (true) {
        List<List<Nodo>> resultados = pool.getResultados();
        if (resultados != null) {
            return resultados;
        } else {
            try {
                Thread.sleep(1000); // Esperamos un segundo antes de volver a comprobar los resultados
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

## Paso 1: Implementación de la clase PoolHebras

La clase `PoolHebras` es la encargada de gestionar el pool de hebras. Deberá contar con un número fijo de hebras, que se crearán al inicializar el pool, es decir en su constructor. El constructor de la clase `PoolHebras` recibirá un número entero que indicará el número de hebras que queremos crear. 

Gestionaremos las tareas que tienen que resolver las hebras a través de una cola de tareas. Las tareas en este caso, serán caminos del grafo que se quieren recorrer, y la tarea consistirá en dar un paso más en el camino. Imaginemos que mientras recorremos el grafo se está analizando un nodo concreto. De ese nodo, podemos obtener los nodos adyacentes (mediante los enlaces salientes a él) que serán los siguientes pasos en el camino, por cada uno de ellos crearemos una nueva tarea. Es importante gestionar los ciclos en el grafo, para ello la tarea debe incluir el nodo que vamos a procesar y los que ya se han procesado en el camino. Definiremos las tareas como instancias de la clase `TareaCamino`, que hemos definido anteriormente, y que contendrá la información necesaria para realizar el recorrido. Por tanto, la cola de tareas será una `List<TareaCamino>`, que contendrá las tareas que se van a procesar. 

De manera similar, la clase PoolHebras debe recoger los resultados de las tareas que se han procesado. Para ello, definiremos una lista de resultados, que contendrá los caminos que se han encontrado. Esta lista será una `List<List<Nodo>>`, que contendrá los caminos que se han encontrado. 

## Paso 1.0: Definición de atributos

La clase `PoolHebras` debe contener (al menos) los siguientes atributos:
- `List<TareaCamino> tareas`: lista de tareas que se van a procesar.
- `List<List<Nodo>> resultados`: lista de resultados que se han encontrado.
- `int numHebras`: número de hebras que se van a crear.
- `List<HebraWorker> hebras`: lista de hebras que se van a crear.

## Paso 1.1: Constructor

El constructor debe inicializar todas las colecciones. En el caso de la colección `hebras`, la llenaremos con un 
número de objetos `HebraWorker` igual al número de hebras que hemos pasado como parámetro. Además de añadir los objetos `HebraWorker` a la lista, debemos iniciar cada hebra. Para ello, utilizaremos el método `start()` de la clase `Thread`, que nos permitirá iniciar la hebra. Este método se encargará de ejecutar el método `run()` de la hebra, que es el que se encargará de procesar las tareas.

## Paso 1.2: búsqueda de caminos

Debemos implementar el método `buscarCaminos(Nodo nodoInicial)`. Este método debe crear una tarea con el nodo inicial y añadirla a la lista de tareas. Para ello, debemos crear un objeto `TareaCamino` con el nodo inicial y una lista vacía de nodos recorridos. Una vez creada la tarea, debemos añadirla a la lista de tareas utilizando el método `addTarea(TareaCamino tarea)`, que se encargará de añadir la tarea a la lista de tareas y despertar a las hebras que están dormidas esperando tareas. Este método se describe en el Paso 2.

## Paso 1.3: obtener resultados

En este paso, implementaremos el método `getResultados()`, que se encargará de devolver la lista de resultados encontrados durante la búsqueda de caminos. Para evitar devolver resultados parciales, si el método es invocado antes de que todas las tareas se hayan procesado, devolveremos `null`. Si no quedan tareas por procesar, devolveremos una copia de la lista de resultados y vaciaremos la lista original. Esto es importante, para no mezclar los resultados de diferentes búsquedas. 

## Paso 2: Gestionar el acceso a las colecciones de tareas y resultados

Las colecciones de tareas y resultados se compartirán entre las hebras, por lo que debemos gestionar el acceso a ellas. Para ello, utilizaremos un diseño monitor, que nos permitirá gestionar el acceso a las colecciones de forma segura. El monitor se implementará utilizando la palabra reservada `synchronized`, que nos permitirá bloquear el acceso a las colecciones mientras una hebra está procesando una tarea. Para cada una de las colecciones debemos definir un método `synchronized` que nos permita añadir elemento y otro que nos permita extraerlos. Seguiremos el mismo esquema que podemos encontrar en la documentación de la asignatura [Sincronización condicional](https://moodle.upm.es/titulaciones/oficiales/pluginfile.php/11967105/mod_resource/content/4/33_Condiciones.pdf).

## Paso 3: Implementación de la clase HebraWorker

La clase `HebraWorker` es la encargada de procesar las tareas. Esta clase debe extender `Threads`, que nos permitirá ejecutar la hebra. La hebra debe estar en un bucle infinito, que se encargará de procesar las tareas de la cola de tareas. Para ello, debemos implementar el método `run()`, que se encargará de procesar las tareas. En este método, debemos extraer una tarea de la cola de tareas y procesarla. Este método debe recorrer el grafo a partir del nodo que se ha pasado como parámetro y añadir los nodos adyacentes a la cola de tareas. Para poder hacer esto, la clase `HebraWorker` debe tener acceso a la clase `PoolHebras` (deberíamos pasar la referencia en el constructor), que contiene la cola de tareas y la lista de resultados. El método run() debe seguir el siguiente esquema:

```java
run() {
    while (true) {
        
        TareaCamino tarea = poolHebras.getTarea();
        List<Nodo> camino = new ArrayList<>();
        camino.addAll(tarea.caminoRecorrido);
        camino.add(tarea.nodoAVisitar);
        if (tarea.nodoAVisitar.getTablero().getMate()){
            poolHebras.addResultado(camino);
        } else {
            for (Enlace enlace : tarea.nodoAVisitar.getEnlacesSalientes()) {
                if (!camino.contains(enlace.getDestino())) {
                    TareaCamino nuevaTarea = new TareaCamino(enlace.getDestino(), camino);
                    poolHebras.addTarea(nuevaTarea);
                }
            }
        }
    }
}
```

El número de posibles caminos en el grafo crece exponencialmente y si no ponemos un límite a la longitud de los caminos, el programa no acabará de procesar todos los caminos posibles, al menos no en este siglo. Por lo tanto, es recomendable poner un límite a la longitud de los caminos. Podemos definir una constante en la clase, de forma que no se exploren caminos que superen una longitud máxima. Deberíamos probar distintos valores en función de lo potente que sea nuestro ordenador, pero un valor de entre 10 y 15 será lo habitual.

## Paso 4: Pruebas

Probamos a hacer búsquedas desde el tablero inicial y comprobamos los caminos obtenidos.
Algunos ejemplos para comprobar el funcionamiento son: 

- Profundidad: 6 // Caminos: 2
- Profundidad: 8 // Caminos: 109
- Profundidad: 10 // Caminos: 520
- Profundidad: 12 // Caminos: 813

## Paso 5: Pasos finales

Con lo que hemos hecho hasta ahora, la clase PoolHebras es capaz de hacer la búsqueda de caminos en el grafo. Sin embargo, una vez acaba la búsqueda las hebras se quedan dormidas esperando indefinidamente a que se les asigne una nueva tarea. Tampoco existe ninguna manera de parar el programa (de manera ordenada) en medio de la búsqueda. Por último, el mecanismo que hemos implementado en el método buscarCaminos para recuperar los resultados, bajo ciertas circunstancias, puede llevar a resultados erróneos. Limitar la profundidad de la búsqueda también es una solución un poco arbitraria. 

Para solucionar estos problemas, vamos a cambiar el funcionamiento de la clase PoolHebras. En primer lugar, en vez de limitar la profundidad de la búsqueda, vamos a limitar el tiempo que le dedicamos a la búsqueda. Para ello estableceremos un tiempo máximo de búsqueda, que se pasará como parámetro al método buscarCaminos. Iniciaremos la búsqueda de la misma manera que antes, pero en lugar de consultar si hay resultados cada segundo, consultaremos si ha pasado el tiempo máximo de búsqueda. Al consultar los resultados tendremos que finalizar las hebras trabajadoras, esperar a que todas ellas acaben y devolver los resultados finales. 

¿Para qué queremos esta funcionalidad? Es un mecanismo habitual en los sistemas concurrentes, queremos que las hebras realicen su trabajo y se terminen cuando ya no hay más trabajo que hacer o bien cuando haya pasado un tiempo determinado. Este tiempo se suele llamar `timeout`.

## Paso 5.1: limitar el tiempo de búsqueda

El primer paso es modificar el método buscarCaminos para que reciba un parámetro adicional que indique el tiempo máximo de búsqueda. Este parámetro será un número entero que indicará el tiempo en milisegundos. En el método, debemos arrancar la búsqueda, dormir la hebra principal durante el tiempo máximo de búsqueda. Tras este tiempo de espera, llamaremos al método `detenerHebras()` que crearemos en el paso 5.4 y por último, llamaremos al método `getResultados()` para obtener los resultados. 

## Paso 5.2: modificar el bucle principal de HebraWorker

Un mecanismo habitual para indicar a las hebras que deben detener su ejecución es el uso de un flag. Este flag es un booleano que indica si la hebra debe detener su ejecución. Añadiremos un atributo `boolean` a la clase `HebraWorker` que indicará si la hebra debe seguir trabajando o no, este atributo siempre se inicializa en `true`. Debemos añadir un método que nos permita cambiar el valor de este atributo desde fuera de la clase. Por último modificaremos el bucle del método `run()` para que compruebe el valor de este atributo. Si el valor es `false`, la hebra debe salir del bucle y terminar.

```java
run() {
    while (seguirTrabajando) { // suponiendo que el atributo se llama seguirTrabajando
        TareaCamino tarea = poolHebras.getTarea();
        if (tarea == null) {
            continue; // Si no hay tarea, volvemos al principio del bucle
        }
        List<Nodo> camino = new ArrayList<>();
        // el resto no cambia
    }
}
```

## Paso 5.3: modificar el método que gestiona el acceso a la lista de tareas

Para evitar que una hebra a la que le hemos indicado que debe parar, se quede bloqueada esperando a que haya tareas, debemos modificar el método en el que gestionamos el acceso a las tareas. En lugar de comprobar únicamente si hay tareas, debemos comprobar si el flag de la hebra está activo. Si no lo está, debemos devolver `null` y salir del bucle. Para poder comprobar este flag, debemos pasar la referencia de la hebra al método `getTarea()`. Este método debe tener la siguiente estructura:

```java
public synchronized TareaCamino getTarea(HebraWorker hebra) {
    while(tareas.isEmpty() && hebra.seguirTrabajando) {
        wait(); // Esperamos a que haya tareas o a que la hebra deba parar
    }
    // Si hemos salido del bucle, significa que hay tareas o que la hebra no debe seguir trabajando
    if (tareas.isEmpty()) {
        return null; // Si no hay tareas, devolvemos null
    }
    // ...
```

## Paso 5.4: añadir el método detenerHebras
El método `detenerHebras()` se encargará de cambiar el flag de todas las hebras a `false`, para que todas ellas salgan del bucle y terminen su ejecución. También esperaremos a que todas las hebras hayan terminado utilizando el método `join()`. El método `join()` no debería ser llamado dentro de un bloque `synchronized`, ya que esto podría provocar un `deadlock` si la hebra que está esperando a que termine la hebra principal, está bloqueada por el monitor. Por lo tanto, debemos asegurarnos de que el método `join()` se llame fuera del bloque `synchronized`. El método `detenerHebras()` debería tener la siguiente estructura:

```java
public void detenerHebras() throws InterruptedException {
    synchronized (this) {
        for (HebraWorker hebra : hebras) {
            hebra.setSeguirTrabajando(false); // Cambiamos el flag de la hebra
        }
        notifyAll(); // Despertamos a todas las hebras
    }
    for (HebraWorker hebra : hebras) {
        hebra.join(); // Esperamos a que la hebra termine
    }
}
```

## Paso 5.5: modificar el método getResultados

Finalmente el método `getResultados()` simplemente debe devolver la lista de resultados y vaciar la lista original. No es necesario hacer nada más, ya que el método `detenerHebras()` se encargará de esperar a que todas las hebras terminen. 

## Paso 5.6: pruebas finales

Las pruebas en este caso son más complicadas, ya que con un tiempo fijo de búsqueda, no podemos garantizar que el número de caminos encontrados sea el mismo. Sin embargo, podemos comprobar que el número de caminos encontrados va creciendo según aumentamos el tiempo de búsqueda. 