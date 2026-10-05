package universidad.ordenamientos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AppTest {

    @Test
    public void deberiaCalcularPromedioDeEstadisticas() {
        List<gestiorDeSesiones.EstadisticaDeAlgoritmo> registros = Arrays.asList(
            new gestiorDeSesiones.EstadisticaDeAlgoritmo("Burbuja Normal", 4, 6, 100.0),
            new gestiorDeSesiones.EstadisticaDeAlgoritmo("Burbuja Normal", 2, 4, 200.0)
        );

        gestiorDeSesiones.ResumenEstadistica resumen = gestiorDeSesiones.calcularPromedio(registros);

        assertEquals(150.0, resumen.getTiempoPromedioMs(), 0.001);
        assertEquals(3.0, resumen.getIntercambiosPromedio(), 0.001);
        assertEquals(5.0, resumen.getComparacionesPromedio(), 0.001);
    }

    @Test
    public void deberiaCrearArchivosDeSesion() throws IOException {
        Path carpetaTemporal = Files.createTempDirectory("ordenamiento-sesion");
        String directorioOriginal = System.getProperty("user.dir");
        System.setProperty("user.dir", carpetaTemporal.toString());

        try {
            gestiorDeSesiones.inicializar();
            Path archivo = carpetaTemporal.resolve("sesiones").resolve("Burbuja Normal.txt");
            assertTrue(Files.exists(archivo));
        } finally {
            System.setProperty("user.dir", directorioOriginal);
        }
    }
}
