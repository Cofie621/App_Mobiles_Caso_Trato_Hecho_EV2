<?php
/**
 * GET api/productos.php
 * Devuelve los productos activos de la tabla "productos".
 * Respuesta: [ { "id": 1, "nombre": "...", "descripcion": "...", "precio": 1500.0, "stock": 10 }, ... ]
 *            (coincide con la clase Producto en Android)
 */
require __DIR__ . '/../config.php';

exigirMetodo('GET');

$filas = db()->query(
    'SELECT id, nombre, descripcion, precio, stock
       FROM productos
      WHERE activo = 1
      ORDER BY nombre'
)->fetchAll();

// MySQL devuelve DECIMAL como texto: se convierte a número para la app
$productos = array_map(fn($p) => [
    'id'          => (int) $p['id'],
    'nombre'      => $p['nombre'],
    'descripcion' => $p['descripcion'],
    'precio'      => (float) $p['precio'],
    'stock'       => (int) $p['stock'],
], $filas);

responder($productos);
