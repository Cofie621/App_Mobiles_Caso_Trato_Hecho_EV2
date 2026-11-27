<?php
/**
 * Configuración común de la API.
 * Todos los archivos de la carpeta api/ incluyen este archivo.
 */

// --- Datos de conexión a MySQL (valores por defecto de XAMPP) ---
const DB_HOST = '127.0.0.1';
const DB_PORT = 3306;
const DB_NAME = 'trato_hecho';
const DB_USER = 'root';
const DB_PASS = '';          // En XAMPP, root no tiene contraseña por defecto

// --- Todas las respuestas son JSON ---
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, ngrok-skip-browser-warning');

// Las peticiones OPTIONS (preflight CORS) se responden vacías
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(204);
    exit;
}

/** Envía una respuesta JSON con el código HTTP indicado y termina. */
function responder($datos, int $codigo = 200): void
{
    http_response_code($codigo);
    echo json_encode($datos, JSON_UNESCAPED_UNICODE);
    exit;
}

/** Lee el cuerpo JSON de la petición (lo que envía Retrofit con @Body). */
function leerJson(): array
{
    $datos = json_decode(file_get_contents('php://input'), true);
    return is_array($datos) ? $datos : [];
}

/** Exige un método HTTP; si no coincide responde 405. */
function exigirMetodo(string ...$metodos): void
{
    if (!in_array($_SERVER['REQUEST_METHOD'], $metodos, true)) {
        responder(['success' => false, 'mensaje' => 'Método no permitido'], 405);
    }
}

/** Devuelve la conexión PDO a MySQL (se crea una sola vez). */
function db(): PDO
{
    static $pdo = null;
    if ($pdo === null) {
        try {
            $pdo = new PDO(
                'mysql:host=' . DB_HOST . ';port=' . DB_PORT . ';dbname=' . DB_NAME . ';charset=utf8mb4',
                DB_USER,
                DB_PASS,
                [
                    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
                    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                    PDO::ATTR_EMULATE_PREPARES   => false,
                ]
            );
        } catch (PDOException $e) {
            responder([
                'success' => false,
                'mensaje' => 'No se pudo conectar a MySQL. ¿Está encendido en XAMPP y se importó database.sql?',
            ], 500);
        }
    }
    return $pdo;
}
