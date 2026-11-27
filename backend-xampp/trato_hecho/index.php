<?php
/**
 * Página de prueba: abre http://localhost/trato_hecho/ (o la URL de ngrok)
 * para comprobar que Apache, PHP y MySQL funcionan.
 */
require __DIR__ . '/config.php';

$productos = (int) db()->query('SELECT COUNT(*) FROM productos WHERE activo = 1')->fetchColumn();

responder([
    'estado'    => 'API Trato Hecho funcionando',
    'mysql'     => 'conectado',
    'productos' => $productos,
    'endpoints' => [
        'POST api/login.php',
        'GET  api/productos.php',
        'GET  api/cotizaciones.php',
        'POST api/cotizaciones.php',
    ],
]);
