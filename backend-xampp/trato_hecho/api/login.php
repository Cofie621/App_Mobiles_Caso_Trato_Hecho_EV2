<?php
/**
 * POST api/login.php
 * Body:      { "tenantId": "...", "correo": "...", "clave": "..." }
 * Respuesta: { "success": true/false, "mensaje": "...", "nombreUsuario": "...", "token": "..." }
 *            (coincide con LoginResponse en ApiService.kt)
 */
require __DIR__ . '/../config.php';

exigirMetodo('POST');

$datos  = leerJson();
$correo = trim($datos['correo'] ?? '');
$clave  = (string) ($datos['clave'] ?? '');

if ($correo === '' || $clave === '') {
    responder(['success' => false, 'mensaje' => 'Debe ingresar correo y contraseña']);
}

$consulta = db()->prepare('SELECT nombre, clave_hash FROM usuarios WHERE correo = ?');
$consulta->execute([$correo]);
$usuario = $consulta->fetch();

if (!$usuario || !password_verify($clave, $usuario['clave_hash'])) {
    // Se responde 200 con success=false para que la app muestre el mensaje
    responder(['success' => false, 'mensaje' => 'Correo o contraseña incorrectos']);
}

responder([
    'success'       => true,
    'mensaje'       => 'Login exitoso',
    'nombreUsuario' => $usuario['nombre'],
    'token'         => bin2hex(random_bytes(16)),
]);
