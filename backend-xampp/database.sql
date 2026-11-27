-- =====================================================================
--  Base de datos de la app "Trato Hecho"
--  Importar en XAMPP: abre http://localhost/phpmyadmin → pestaña "Importar"
--  → elige este archivo → "Importar" (o "Continuar").
-- =====================================================================

SET NAMES utf8mb4;   -- evita que los acentos (á, é, ñ...) se guarden mal

CREATE DATABASE IF NOT EXISTS trato_hecho
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE trato_hecho;

-- Se borran en orden inverso por las claves foráneas (permite reimportar)
DROP TABLE IF EXISTS cotizacion_detalle;
DROP TABLE IF EXISTS cotizaciones;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS usuarios;

-- ---------------------------------------------------------------------
--  Tabla de PRODUCTOS: lo que la app muestra para armar la cotización
-- ---------------------------------------------------------------------
CREATE TABLE productos (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  nombre      VARCHAR(100)   NOT NULL,
  descripcion VARCHAR(255)   NULL,
  precio      DECIMAL(12, 2) NOT NULL,
  stock       INT            NOT NULL DEFAULT 0,
  activo      TINYINT(1)     NOT NULL DEFAULT 1,   -- 1 = se muestra en la app
  creado_en   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

INSERT INTO productos (nombre, descripcion, precio, stock) VALUES
  ('Notebook 14"',          'Intel Core i5, 16 GB RAM, 512 GB SSD', 549990, 10),
  ('Monitor 24"',           'Full HD IPS, 75 Hz',                   129990, 25),
  ('Teclado mecánico',      'Switch red, retroiluminado',            39990, 40),
  ('Mouse inalámbrico',     '2.4 GHz, 1600 DPI',                     12990, 60),
  ('Impresora láser',       'Monocromática, WiFi',                  119990,  8),
  ('Disco SSD 1 TB',        'NVMe M.2',                              69990, 30),
  ('Licencia Office 365',   'Plan anual, 1 usuario',                 59990, 100),
  ('Soporte técnico (hora)','Servicio en terreno',                   25000, 999);

-- ---------------------------------------------------------------------
--  Tabla de USUARIOS: quienes pueden iniciar sesión en la app
--  La contraseña se guarda cifrada (bcrypt). Ambos usuarios usan: 1234
-- ---------------------------------------------------------------------
CREATE TABLE usuarios (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  nombre     VARCHAR(100) NOT NULL,
  correo     VARCHAR(150) NOT NULL UNIQUE,
  clave_hash VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

INSERT INTO usuarios (nombre, correo, clave_hash) VALUES
  ('Administrador', 'admin@tratohecho.cl',    '$2y$10$4Lar0r2w16lSOM.t3IQuAODRK9ahxoXOwYl3zeT90vOAmj0Pw0tGC'),
  ('Vendedor',      'vendedor@tratohecho.cl', '$2y$10$4Lar0r2w16lSOM.t3IQuAODRK9ahxoXOwYl3zeT90vOAmj0Pw0tGC');

-- ---------------------------------------------------------------------
--  Tablas de COTIZACIONES (cabecera + detalle de productos)
-- ---------------------------------------------------------------------
CREATE TABLE cotizaciones (
  id        INT AUTO_INCREMENT PRIMARY KEY,
  tenant_id VARCHAR(100)   NOT NULL,          -- usuario/empresa que la creó
  moneda    VARCHAR(10)    NOT NULL DEFAULT 'CLP',
  subtotal  DECIMAL(12, 2) NOT NULL,
  impuesto  DECIMAL(12, 2) NOT NULL,
  total     DECIMAL(12, 2) NOT NULL,
  fecha     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE cotizacion_detalle (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  cotizacion_id  INT            NOT NULL,
  producto_id    INT            NULL,           -- producto de la tabla productos
  descripcion    VARCHAR(255)   NOT NULL,       -- nombre del producto al momento de cotizar
  precio         DECIMAL(12, 2) NOT NULL,
  cantidad       INT            NOT NULL DEFAULT 1,
  nombre_cliente VARCHAR(150)   NOT NULL DEFAULT '',
  FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE CASCADE,
  FOREIGN KEY (producto_id)   REFERENCES productos(id)    ON DELETE SET NULL
) ENGINE=InnoDB;
