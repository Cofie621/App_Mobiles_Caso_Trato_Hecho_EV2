const express = require("express");
const cors = require("cors");

const app = express();
const PORT = 8080;

app.use(cors());
app.use(express.json());

// Almacenamiento temporal en memoria
let cotizaciones = [];

// Ruta de prueba para verificar que la API está activa
app.get("/", (req, res) => {
  res.json({ status: "online", mensaje: "API Trato Hecho funcionando" });
});

// 1. Login de trabajadores (flexible para cualquier trabajador de la empresa)
app.post("/api/login", (req, res) => {
  const { tenantId, correo, clave } = req.body;
  console.log(`${new Date().toLocaleTimeString()} - POST /api/login:`, { tenantId, correo });

  if (!correo || !clave) {
    return res.status(400).json({
      success: false,
      mensaje: "Debe ingresar correo y contraseña"
    });
  }

  const nombreTrabajador = tenantId && tenantId.trim() !== ""
    ? tenantId
    : correo.split("@")[0];

  res.json({
    success: true,
    mensaje: "Login exitoso",
    nombreUsuario: nombreTrabajador,
    token: `token-${Date.now()}`
  });
});

// 2. Guardar nueva cotización
app.post("/api/cotizaciones", (req, res) => {
  const { tenantId, moneda, items, subtotal, impuesto, total } = req.body;
  console.log(`${new Date().toLocaleTimeString()} - POST /api/cotizaciones:`, req.body);

  const nuevaCotizacion = {
    id: Date.now(),
    tenantId,
    moneda,
    items,
    subtotal,
    impuesto,
    total,
    fecha: new Date().toISOString()
  };

  cotizaciones.push(nuevaCotizacion);

  res.status(201).json({
    success: true,
    mensaje: "Cotización recibida correctamente",
    cotizacion: nuevaCotizacion
  });
});

// 3. Consultar historial de cotizaciones
app.get("/api/cotizaciones", (req, res) => {
  console.log(`${new Date().toLocaleTimeString()} - GET /api/cotizaciones`);
  res.json(cotizaciones);
});

app.listen(PORT, () => {
  console.log(`API Trato Hecho escuchando en http://localhost:${PORT}`);
  console.log(`Para publicarla con ngrok:  ngrok http ${PORT}`);
});
