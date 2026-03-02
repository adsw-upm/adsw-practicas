package es.upm.dit.adsw.cifrasyletras;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import es.upm.dit.adsw.cifrasyletras.letras.LetrasPractica;

/**
 * Suite de pruebas unitarias para la clase LetrasPractica.
 * El método vectorizarLetras, las colecciones palabrasValidas y palabrasOrdenadas deben ser public.
 */
class TestPractica1 {

	private static LetrasPractica letras;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		letras = new LetrasPractica();
	}


	@Test
	@DisplayName("Validación de carga de diccionario")
	void testMapaPalabras() {
		assertNotNull(letras.palabrasValidas, "Error: El mapa 'palabrasValidas' no ha sido instanciado.");
		assertEquals(636598, letras.palabrasValidas.size(), 
			"Error: El número de palabras cargadas no coincide con el total esperado del diccionario.");
	}
	
	@Test
	@DisplayName("Validación de vectores de frecuencia en diccionario")
	void testVectoresPalabras() {
		// Verificación para "programacion"
		Map<Character, Integer> frecProgramacion = new HashMap<>();
		frecProgramacion.put('p', 1); frecProgramacion.put('c', 1); frecProgramacion.put('g', 1);
		frecProgramacion.put('i', 1); frecProgramacion.put('m', 1); frecProgramacion.put('n', 1);
		frecProgramacion.put('o', 2); frecProgramacion.put('a', 2); frecProgramacion.put('r', 2);
		
		assertNotNull(letras.palabrasValidas.get("programacion"), "Error: 'programacion' no se encuentra en el mapa.");
		assertEquals(frecProgramacion, letras.palabrasValidas.get("programacion"), 
			"Error: El mapa de frecuencias de 'programacion' es incorrecto.");
		
		// Verificación para "algoritmo"
		Map<Character, Integer> frecAlgoritmo = new HashMap<>();
		frecAlgoritmo.put('a', 1); frecAlgoritmo.put('r', 1); frecAlgoritmo.put('t', 1);
		frecAlgoritmo.put('g', 1); frecAlgoritmo.put('i', 1); frecAlgoritmo.put('l', 1);
		frecAlgoritmo.put('m', 1); frecAlgoritmo.put('o', 2);
		
		assertNotNull(letras.palabrasValidas.get("algoritmo"), "Error: 'algoritmo' no se encuentra en el mapa.");
		assertEquals(frecAlgoritmo, letras.palabrasValidas.get("algoritmo"), 
			"Error: El mapa de frecuencias de 'algoritmo' es incorrecto.");
		
		// Verificación de inexistencia
		assertNull(letras.palabrasValidas.get("algoristmo"), "Error: 'algoristmo' (inexistente) ha sido encontrado en el mapa.");
	}
	

	@Test
	@DisplayName("Validación de lista de palabras ordenadas por longitud")
	void testListaPalabrasOrdenada() {
		// Verificación de integridad de la estructura
		assertNotNull(letras.palabrasOrdenadas, 
			"Error: La lista 'palabrasOrdenadas' no ha sido inicializada en el constructor.");

		// Verificación de volumen de datos
		assertEquals(636598, letras.palabrasOrdenadas.size(), 
			"Error: La lista ordenada no contiene el mismo número de elementos que el mapa de palabras.");

		// Verificación de ordenación descendente (Extremo superior)
		assertEquals(18, letras.palabrasOrdenadas.get(0).length(), 
			"Error: El primer elemento de la lista debe ser una palabra de longitud máxima (18).");

		// Verificación de ordenación descendente (Extremo inferior)
		assertEquals(2, letras.palabrasOrdenadas.get(letras.palabrasOrdenadas.size() - 1).length(), 
			"Error: El último elemento de la lista debe ser una palabra de longitud mínima (2).");
	}

	@Test
	@DisplayName("Pruebas de búsqueda de palabra (obtenerPalabra)")
	void testObtenerPalabra() {
		// Caso sin solución
		assertNull(letras.obtenerPalabra("ñññññññññ"), "Error: Debería devolver null para letras sin palabras posibles.");

		// Caso palabra exacta
		assertNotNull(letras.obtenerPalabra("algoritmo"), "Error: No se encontró la palabra 'algoritmo' teniendo las letras exactas.");
		assertEquals("algoritmo".length(), letras.obtenerPalabra("algoritmo").length(), 
			"Error: La longitud de la palabra encontrada no coincide con la esperada.");

		// Caso con letras sobrantes (ruido)
		assertNotNull(letras.obtenerPalabra("algoritmoxxx"), "Error: Fallo al encontrar 'algoritmo' con letras adicionales.");
		assertEquals("algoritmo".length(), letras.obtenerPalabra("algoritmoxxx").length(), 
			"Error: No se priorizó o encontró la palabra correcta con ruido en el input.");
		
		// Caso de longitud mínima esperada
		assertNotNull(letras.obtenerPalabra("asaxxxxxxxx"), "Error: No se encontró una palabra corta con mucho ruido.");
		assertEquals(3, letras.obtenerPalabra("asaxxxxxxxx").length(), 
			"Error: Se esperaba una palabra de longitud 3 para el input 'asaxxxxxxxx'.");
	}

	/**
	 * Valida el método público/protegido de generación de mapas de frecuencia 
	 * a partir de cadenas arbitrarias.
	 */
	@Test
	@DisplayName("Validación de utilidad vectorizarLetras")
	void testVectorizarPalabra() {
		try {
			assertNull(letras.vectorizarLetras(null), "Error: El vector de un String nulo debería ser nulo o lanzar excepción controlada.");
		} catch (Exception e) {
			// Excepción capturada correctamente según diseño
		}
		
		Map<Character, Integer> frecAlgoritmo = new HashMap<>();
		frecAlgoritmo.put('a', 1); frecAlgoritmo.put('r', 1); frecAlgoritmo.put('t', 1);
		frecAlgoritmo.put('g', 1); frecAlgoritmo.put('i', 1); frecAlgoritmo.put('l', 1);
		frecAlgoritmo.put('m', 1); frecAlgoritmo.put('o', 2);
		
		assertEquals(frecAlgoritmo, letras.vectorizarLetras("algoritmo"), "Error: Frecuencias generadas incorrectas para 'algoritmo'.");
		
		frecAlgoritmo.put('x', 3);
		assertEquals(frecAlgoritmo, letras.vectorizarLetras("algoritmoxxx"), "Error: Frecuencias generadas incorrectas con caracteres repetidos.");
	}
	

//	@Test
//	@DisplayName("Validación de lógica de comodines")
//	void testComodin() {
//		String res1 = letras.obtenerPalabra("algoritm?");
//		assertNotNull(res1, "Error: No se encontró palabra usando un comodín.");
//		assertEquals(9, res1.length(), "Error: El comodín no completó una palabra de 9 letras.");
//
//		String res2 = letras.obtenerPalabra("algorit??");
//		assertNotNull(res2, "Error: No se encontró palabra usando dos comodines.");
//		assertEquals(9, res2.length(), "Error: Los dos comodines no completaron una palabra de 9 letras.");
//
//		String res3 = letras.obtenerPalabra("?????????");
//		assertNotNull(res3, "Error: El bot no pudo encontrar ninguna palabra de 9 letras usando solo comodines.");
//		assertEquals(9, res3.length(), "Error: Se esperaba una palabra de 9 letras para un input de 9 comodines.");
//	}
	
	
}