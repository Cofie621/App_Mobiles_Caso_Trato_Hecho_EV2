/**
 * API REST para la app "Trato Hecho".
 *
 * Endpoints (los mismos que usa ApiService.kt en Android):
 *   POST /api/login          → inicia sesión
 *   POST /api/cotizaciones   → guarda una cotización
 *   GET  /api/cotizaciones   → lista los productos de las cotizaciones guardadas
 *
 * Para correrla:   npm install   y luego   npm start
 * Para publicarla: ngrok http 8080
 */

const express = require("express");

const app = express();
const PORT = process.env.PORT || 8080;

// Permite recibir JSON en el body de las peticiones
app.use(express.json());

// Muestra en la consola cada petición que llega (útil para depurar)
app.use((req, res, next) => {
  console.log(`${new Date().toLocaleTimeString()}  ${req.method} ${req.url}`);
  next();
});

// ---------------------------------------------------------------------------
// "Base de datos" en memoria (se borra al reiniciar el servidor)
// ---------------------------------------------------------------------------

// Usuarios de prueba que pueden iniciar sesión
const usuarios = [
  { correo: "admin@tratohecho.cl", clave: "1234", nombre: "Administrador" },
  { correo: "vendedor@tratohecho.cl", clave: "1234", nombre: "Vendedor" },
];

// Cotizaciones recibidas desde la app
const cotizaciones = [];

// ---------------------------------------------------------------------------
// Rutas
// ---------------------------------------------------------------------------

// Ruta de prueba: abre la URL de ngrok en el navegador para ver si funciona
app.get("/", (req, res) => {
  res.json({ estado: "API Trato Hecho funcionando" });
});

/**
 * POST /api/login
 * Body:      { tenantId, correo, clave }
 * Respuesta: { success, mensaje, nombreUsuario, token }   (LoginResponse en Android)
 */
app.post("/api/login", (req, res) => {
  const { tenantId, correo, clave } = req.body || {};

  if (!tenantId || !correo || !clave) {
    return res.json({ success: false, mensaje: "Faltan datos para iniciar sesión" });
  }

  const usuario = usuarios.find(
    (u) => u.correo.toLowerCase() === String(correo).toLowerCase() && u.clave === clave
  );

  if (!usuario) {
    return res.json({ success: false, mensaje: "Correo o contraseña incorrectos" });
  }

  res.json({
    success: true,
    mensaje: "Login exitoso",
    nombreUsuario: usuario.nombre,
    token: `token-${Date.now()}`, // Token simple de ejemplo
  });
});

/**
 * POST /api/cotizaciones
 * Body: { tenantId, moneda, items: [{ descripcion, precio, cantidad, NombreCliente }],
 *         subtotal, impuesto, total }                       (CotizacionRequest en Android)
 */
app.post("/api/cotizaciones", (req, res) => {
  const { tenantId, moneda, items, subtotal, impuesto, total } = req.body || {};

  if (!Array.isArray(items) || items.length === 0) {
    return res.status(400).json({ success: false, mensaje: "La cotización no tiene productos" });
  }

  const nueva = {
    id: cotizaciones.length + 1,
    tenantId,
    moneda,
    items,
    subtotal,
    impuesto,
    total,
    fecha: new Date().toISOString(),
  };
  cotizaciones.push(nueva);

  res.status(201).json({ success: true, id: nueva.id, mensaje: "Cotización guardada" });
});

/**
 * GET /api/cotizaciones
 * Respuesta: lista de productos [{ descripcion, precio, cantidad, NombreCliente }]
 * (en Android: List<CotizacionItem>)
 */
app.get("/api/cotizaciones", (req, res) => {
  const items = cotizaciones.flatMap((c) => c.items);
  res.json(items);
});

// ---------------------------------------------------------------------------

app.listen(PORT, () => {
  console.log(`API Trato Hecho escuchando en http://localhost:${PORT}`);
  console.log(`Para publicarla con ngrok:  ngrok http ${PORT}`);
});
