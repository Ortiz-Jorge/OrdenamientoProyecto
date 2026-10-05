package universidad.ordenamientos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class gestiorDeSesiones {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void inicializar() {
        try {
            Files.createDirectories(obtenerDirectorioSesiones());
            for (String nombreAlgoritmo : obtenerNombresDeAlgoritmos()) {
                crearArchivoSiNoExiste(nombreAlgoritmo);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el gestor de sesiones.", e);
        }
    }

    public static void guardarResultado(String nombreAlgoritmo, long intercambios, long comparaciones, double tiempoMs) {
        inicializar();

        Path archivo = obtenerRutaDeArchivo(nombreAlgoritmo);
        String linea = String.format(
                Locale.US,
                "%s | intercambios=%d | comparaciones=%d | tiempoMs=%.2f%n",
                LocalDateTime.now().format(FORMATO_FECHA),
                intercambios,
                comparaciones,
                tiempoMs
        );

        try {
            Files.writeString(
                    archivo,
                    linea,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el resultado del algoritmo " + nombreAlgoritmo, e);
        }
    }

    public static void mostrarResumenGeneral() {
        System.out.println("\n========================================");
        System.out.println("PROMEDIO DE SESIONES POR ALGORITMO");
        System.out.println("========================================");

        for (String nombreAlgoritmo : obtenerNombresDeAlgoritmos()) {
            List<EstadisticaDeAlgoritmo> historial = leerRegistros(nombreAlgoritmo);
            ResumenEstadistica resumen = calcularPromedio(historial);

            System.out.printf("%s:%n", nombreAlgoritmo);
            System.out.printf("  Ejecutadas: %d%n", resumen.getCantidadEjecuciones());
            System.out.printf("  Tiempo promedio: %.2f ms%n", resumen.getTiempoPromedioMs());
            System.out.printf("  Intercambios promedio: %.2f%n", resumen.getIntercambiosPromedio());
            System.out.printf("  Comparaciones promedio: %.2f%n", resumen.getComparacionesPromedio());
            System.out.println("----------------------------------------");
        }
    }

    public static ResumenEstadistica calcularPromedio(List<EstadisticaDeAlgoritmo> registros) {
        if (registros == null || registros.isEmpty()) {
            return new ResumenEstadistica(0, 0, 0, 0);
        }

        double tiempoPromedio = registros.stream()
                .mapToDouble(EstadisticaDeAlgoritmo::getTiempoMs)
                .average()
                .orElse(0.0);

        double intercambioPromedio = registros.stream()
                .mapToDouble(EstadisticaDeAlgoritmo::getIntercambios)
                .average()
                .orElse(0.0);

        double comparacionPromedio = registros.stream()
                .mapToDouble(EstadisticaDeAlgoritmo::getComparaciones)
                .average()
                .orElse(0.0);

        return new ResumenEstadistica(tiempoPromedio, intercambioPromedio, comparacionPromedio, registros.size());
    }

    public static List<EstadisticaDeAlgoritmo> leerRegistros(String nombreAlgoritmo) {
        Path archivo = obtenerRutaDeArchivo(nombreAlgoritmo);
        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }

        List<EstadisticaDeAlgoritmo> registros = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            for (String linea : lineas) {
                if (linea == null || linea.trim().isEmpty() || linea.startsWith("Algoritmo:")) {
                    continue;
                }

                String[] partes = linea.split("\\s+\\|\\s+");
                if (partes.length < 4) {
                    continue;
                }

                String fecha = partes[0];
                long intercambios = extraerLong(partes[1], "intercambios=");
                long comparaciones = extraerLong(partes[2], "comparaciones=");
                double tiempoMs = extraerDouble(partes[3], "tiempoMs=");

                registros.add(new EstadisticaDeAlgoritmo(nombreAlgoritmo, intercambios, comparaciones, tiempoMs, fecha));
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el historial del algoritmo " + nombreAlgoritmo, e);
        }

        return registros;
    }

    private static void crearArchivoSiNoExiste(String nombreAlgoritmo) throws IOException {
        Path archivo = obtenerRutaDeArchivo(nombreAlgoritmo);
        if (!Files.exists(archivo)) {
            Files.writeString(
                    archivo,
                    "Algoritmo: " + nombreAlgoritmo + "\n",
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        }
    }

    private static Path obtenerDirectorioSesiones() {
        return Paths.get(System.getProperty("user.dir"), "sesiones");
    }

    private static Path obtenerRutaDeArchivo(String nombreAlgoritmo) {
        return obtenerDirectorioSesiones().resolve(sanitizarNombre(nombreAlgoritmo) + ".txt");
    }

    private static String sanitizarNombre(String nombreAlgoritmo) {
        return nombreAlgoritmo.replace("/", "-")
                .replace("\\", "-")
                .replace(":", "-")
                .replace("*", "-")
                .replace("?", "-")
                .replace("\"", "-")
                .replace("<", "-")
                .replace(">", "-")
                .replace("|", "-")
                .trim();
    }

    private static List<String> obtenerNombresDeAlgoritmos() {
        List<String> nombres = new ArrayList<>();
        nombres.add("Burbuja Normal");
        nombres.add("Burbuja Mejorado");
        nombres.add("Insertion");
        nombres.add("Seleccion");
        nombres.add("Quick Sort");
        return nombres;
    }

    private static long extraerLong(String valor, String prefijo) {
        String texto = valor.trim();
        if (texto.startsWith(prefijo)) {
            return Long.parseLong(texto.substring(prefijo.length()));
        }
        return 0L;
    }

    private static double extraerDouble(String valor, String prefijo) {
        String texto = valor.trim();
        if (texto.startsWith(prefijo)) {
            String numero = texto.substring(prefijo.length()).replace(',', '.');
            return Double.parseDouble(numero);
        }
        return 0.0;
    }

    public static class EstadisticaDeAlgoritmo {
        private final String nombre;
        private final long intercambios;
        private final long comparaciones;
        private final double tiempoMs;
        private final String fecha;

        public EstadisticaDeAlgoritmo(String nombre, long intercambios, long comparaciones, double tiempoMs) {
            this(nombre, intercambios, comparaciones, tiempoMs, LocalDateTime.now().format(FORMATO_FECHA));
        }

        public EstadisticaDeAlgoritmo(String nombre, long intercambios, long comparaciones, double tiempoMs, String fecha) {
            this.nombre = nombre;
            this.intercambios = intercambios;
            this.comparaciones = comparaciones;
            this.tiempoMs = tiempoMs;
            this.fecha = fecha;
        }

        public String getNombre() {
            return nombre;
        }

        public long getIntercambios() {
            return intercambios;
        }

        public long getComparaciones() {
            return comparaciones;
        }

        public double getTiempoMs() {
            return tiempoMs;
        }

        public String getFecha() {
            return fecha;
        }
    }

    public static class ResumenEstadistica {
        private final double tiempoPromedioMs;
        private final double intercambiosPromedio;
        private final double comparacionesPromedio;
        private final int cantidadEjecuciones;

        public ResumenEstadistica(double tiempoPromedioMs, double intercambiosPromedio, double comparacionesPromedio, int cantidadEjecuciones) {
            this.tiempoPromedioMs = tiempoPromedioMs;
            this.intercambiosPromedio = intercambiosPromedio;
            this.comparacionesPromedio = comparacionesPromedio;
            this.cantidadEjecuciones = cantidadEjecuciones;
        }

        public double getTiempoPromedioMs() {
            return tiempoPromedioMs;
        }

        public double getIntercambiosPromedio() {
            return intercambiosPromedio;
        }

        public double getComparacionesPromedio() {
            return comparacionesPromedio;
        }

        public int getCantidadEjecuciones() {
            return cantidadEjecuciones;
        }
    }
}
