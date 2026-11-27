<?php
/**
 * POST api/cotizaciones.php  → guarda una cotización y sus productos
 *   Body: { "tenantId", "moneda", "subtotal", "impuesto", "total",
 *           "items": [ { "productoId", "descripcion", "precio", "cantidad", "NombreCliente" } ] }
 *
 * GET  api/cotizaciones.php  → lista los productos cotizados
 *   Respuesta: [ { "productoId", "descripcion", "precio", "cantidad", "NombreCliente" }, ... ]
 *              (coincide con List<CotizacionItem> en Android)
 */
require __DIR__ . '/../config.php';

exigirMetodo('GET', 'POST');

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $filas = db()->query(
        'SELECT d.producto_id, d.descripcion, d.precio, d.cantidad, d.nombre_cliente
           FROM cotizacion_detalle d
           JOIN cotizaciones c ON c.id = d.cotizacion_id
          ORDER BY c.fecha DESC, d.id'
    )->fetchAll();

    responder(array_map(fn($f) => [
        'productoId'    => $f['producto_id'] !== null ? (int) $f['producto_id'] : null,
        'descripcion'   => $f['descripcion'],
        'precio'        => (float) $f['precio'],
        'cantidad'      => (int) $f['cantidad'],
        'NombreCliente' => $f['nombre_cliente'],
    ], $filas));
}

// ---------------- POST: guardar cotización ----------------
$datos = leerJson();
$items = $datos['items'] ?? [];

if (!is_array($items) || count($items) === 0) {
    responder(['success' => false, 'mensaje' => 'La cotización no tiene productos'], 400);
}

$pdo = db();
try {
    // Transacción: se guarda todo o nada
    $pdo->beginTransaction();

    $cabecera = $pdo->prepare(
        'INSERT INTO cotizaciones (tenant_id, moneda, subtotal, impuesto, total)
         VALUES (?, ?, ?, ?, ?)'
    );
    $cabecera->execute([
        trim($datos['tenantId'] ?? '') ?: 'sin-usuario',
        $datos['moneda'] ?? 'CLP',
        (float) ($datos['subtotal'] ?? 0),
        (float) ($datos['impuesto'] ?? 0),
        (float) ($datos['total'] ?? 0),
    ]);
    $cotizacionId = (int) $pdo->lastInsertId();

    $detalle = $pdo->prepare(
        'INSERT INTO cotizacion_detalle
            (cotizacion_id, producto_id, descripcion, precio, cantidad, nombre_cliente)
         VALUES (?, ?, ?, ?, ?, ?)'
    );
    foreach ($items as $item) {
        $productoId = isset($item['productoId']) ? (int) $item['productoId'] : null;
        $detalle->execute([
            $cotizacionId,
            $productoId ?: null,
            $item['descripcion'] ?? '',
            (float) ($item['precio'] ?? 0),
            max(1, (int) ($item['cantidad'] ?? 1)),
            $item['NombreCliente'] ?? '',
        ]);
    }

    $pdo->commit();
} catch (PDOException $e) {
    $pdo->rollBack();
    responder(['success' => false, 'mensaje' => 'Error al guardar la cotización'], 500);
}

responder(['success' => true, 'id' => $cotizacionId, 'mensaje' => 'Cotización guardada'], 201);
