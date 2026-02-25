# ADSW Práctica 1: Mejora de la resolución automática de Letras

En el laboratorio anterior resolvimos automáticamente la prueba de Letras mediante una estrategia de generación de combinaciones.

En esta práctica vamos a mejorar ese enfoque utilizando una estructura de datos más adecuada.

> El programa debe encontrar automáticamente una palabra válida que pueda formarse con las letras dadas, priorizando las palabras más largas.

---

## Objetivo de la práctica

Implementar la clase:

- `LetrasPractica`

en el paquete:

- `es.upm.dit.adsw.cifrasyletras.letras`

Esta clase implementa la interfaz `Letras` y debe definir el método:

```java
public String obtenerPalabra(String letras);
```

A diferencia del laboratorio anterior, ahora no generaremos combinaciones explícitamente. Trabajaremos
directamente sobre el listado de palabras válidas completo, utilizando una estructura que permita recorrer las palabras
en un orden adecuado.

---

## Enfoque conceptual

La idea general es la siguiente:

1. Preprocesar el listado de palabras válidas:
   - Para cada palabra válida, calcular la frecuencia de aparición de cada letra.
   - Guardar esa información en una estructura que permita consultarla rápidamente.

2. Dada una cadena de letras proporcionada al jugador:
   -  Calcular también la frecuencia de cada letra.
   - Recorrer el listado de palabras válidas y comprobar si alguna palabra puede formarse con esas letras.

Una palabra será válida si, para cada carácter que utiliza, la frecuencia en la palabra es menor o igual que la frecuencia disponible en el conjunto inicial.

---

## Parte 1: Implementación básica con `HashMap`

En una primera versión, se propone utilizar:

`HashMap<String, Map<Character, Integer>> mapaPalabras`

donde:

- La clave es la palabra válida.
- El valor es un mapa que contiene el número de veces que aparece cada carácter en dicha palabra.

Ejemplo conceptual:

Palabra: contratos  
Diccionario asociado:  
c → 1  
o → 2  
n → 1  
t → 2  
r → 1  
a → 1  
s → 1

### Qué debe hacer el constructor

- Leer el fichero `data/es.txt`.
- Normalizar cada palabra (minúsculas, sin tildes, sin espacios).
- Construir el mapa de frecuencias de cada palabra. Se recomienda hacer un método `vectorizarPalabra` al que le pasamos un `String` y nos devuelve el mapa de frecuencias correspondiente.
- Insertar la palabra y su mapa en `mapaPalabras`.

> [!TIP]
> Recuerda que para leer el fichero podemos utilizar la clase `BufferedReader`, leyendo cada línea igual que se hace en
> el constructor proporcionado en el Laboratorio 1.
> Para normalizar las palabras se puede reutilizar el método de la clase `ValidadorLetras`.

---

## Parte 2: Implementación de `obtenerPalabra`

El método debe:

1. Normalizar las letras recibidas.
2. Construir un mapa de frecuencias para dichas letras. Puede usarse el método `vectorizarPalabra` que definimos en el paso anterior.
3. Recorrer las palabras del diccionario.
4. Para cada palabra:
   - Comprobar que todas sus letras existen en el conjunto inicial.
   - Comprobar que la frecuencia de cada letra no supera la disponible.
5. Devolver la primera palabra que cumpla las condiciones.

Si no existe ninguna palabra posible, devolver `null`.

### Observación importante

En esta versión básica, la primera palabra válida encontrada no tiene por qué ser la más larga posible. Esto dependerá del orden interno del `HashMap`, que no está definido.

### Visualización del algoritmo (comparación de frecuencias)

La siguiente animación muestra un ejemplo de cómo el algoritmo debería realizar la comparación de las frecuencias de
las palabras del listado de palabras válidas, con las frecuencias de las letras proporcionadas al jugador.

![Visualización de la comparación de fruencias](data/P1ValidacionDiccionario.gif)

▶️ **[Abrir animación completa (MP4, con controles de reproducción)](https://drive.upm.es/s/GLfeyb4rnTx4ZNp)**

> Recomendación: abre el MP4 si quieres pausar el vídeo, avanzar y retroceder para seguir con calma cada paso.

---

## Parte 3: Priorizar palabras más largas

Si recorriéramos el listado de palabras ordenado de mayor a menor longitud, la primera palabra válida encontrada sería
necesariamente la mejor posible (es decir, la más larga que puede formarse con las letras dadas).

Sin embargo, `HashMap` no mantiene ningún orden interno, por lo que no podemos controlar en qué orden se recorren las
palabras, ni tampoco guardarlas ordenadas.

> [!IMPORTANT]
> ¿Qué otras estructuras mantienen un orden?

- `LinkedHashMap` mantiene el orden de inserción.
- `TreeMap` mantiene las claves ordenadas según su orden natural o según un `Comparator`.

En nuestro caso necesitamos ordenar las palabras por un criterio personalizado
(longitud descendente). Para ello utilizaremos un `TreeMap` definiendo un `Comparator`.

El comparador deberá:

1. Ordenar primero por longitud (de mayor a menor).
2. En caso de empate, ordenar lexicográficamente.

De este modo:

- Las palabras más largas se evaluarán antes.
- La primera coincidencia será automáticamente la mejor solución posible.

---

# Parte 4 (Opcional): Soporte de comodín

De forma opcional, se propone ampliar la práctica para permitir el uso de un comodín.

En esta versión, el comodín estará representado por el carácter:

`?`

y formará parte de la cadena de letras recibida en el método `obtenerPalabra`.

Ejemplo:

`aacvben?`

indica que el jugador dispone de las letras `a a c v b e n` y un comodín.

---

## Funcionamiento del comodín

El carácter `?` representa una ficha en blanco que puede sustituir a cualquier letra, pero solo una vez.

Es decir:

- Cada `?` permite cubrir una letra faltante.
- Si faltan dos letras, se necesitarán dos `?`.
- El comodín se “gasta” cada vez que se utiliza.
- Si la cadena contiene varios `?`, cada uno contará como un comodín independiente.

---

## Qué hay que modificar

1. Al procesar la cadena `letras`:
   - Contar cuántos caracteres `?` aparecen.
   - No incluir `?` en el mapa de frecuencias de letras.
   - Guardar el número de comodines disponibles.

2. Al comprobar si una palabra puede formarse:
   - Calcular cuántas veces aparece cada letra en la palabra.
   - Comparar con las letras disponibles.
   - Si para alguna letra faltan ocurrencias:
      - Restar esa diferencia al número de comodines.
      - Si el número de comodines se vuelve negativo, la palabra no es válida.

---

## Ejemplo

Letras dadas:

`casa?`

- `casar` → válida (falta una `r`, se usa el comodín)
- `casara` → no válida (faltan dos letras y solo hay un comodín)

---

## Consideraciones

- La longitud máxima de la palabra será igual a `letras.length()`.  
  (El comodín ya forma parte del conjunto de letras disponibles.)
- La estructura general del algoritmo no cambia; únicamente se ajusta la comprobación de validez.
- La palabra devuelta debe seguir siendo la más larga posible.
