🏢 Empresa
Progela - Empresa Farmacéutica

📱 Nombre del Proyecto
App Móvil de Gestión Comercial - Progela

🧩 Descripción General
La aplicación móvil desarrollada en Android Studio con Java está diseñada para facilitar y digitalizar la gestión de actividades de los representantes de farmacia y supervisores de la empresa Progela. Implementa funcionalidades clave para la toma de decisiones, control de rutas, pedidos y seguimiento comercial, todo ello siguiendo el patrón de diseño Modelo - Vista - Controlador (MVC).

🧠 Arquitectura del Proyecto
Plataforma: Android

Lenguaje de Programación: Java

IDE: Android Studio

Patrón de diseño: Modelo - Vista - Controlador (MVC)

Modos de conexión:

Online (LAN o VPN Cisco Security)

Offline (almacenamiento local con sincronización posterior)

👥 Tipos de Usuarios
Representante de Farmacia
Accede y ejecuta tareas individuales.

Supervisor de Zona
Tiene visibilidad sobre toda la operación de sus representantes asignados.

🔒 Accesibilidad y Seguridad
Acceso a los servicios del servidor:

Si el dispositivo está en red local (LAN) → acceso directo.

Si el dispositivo está fuera de la red → requiere conexión mediante VPN Cisco Security.

Mecanismos de autenticación y validación de roles.

🧩 Módulos Principales
📌 1. Registrar Prospectos
Formulario para captura de nuevos posibles clientes.

Datos capturados: nombre del negocio, contacto, dirección, tipo de cliente, etc.

Sincronización con servidor (modo online o modo offline con reintento).

📦 2. Levantar Pedidos
Selección de productos desde catálogo.

Gestión de cantidades, precios y totales.

Registro de pedido con sincronización automática o diferida.

📸 3. Enviar Asistencia con Foto
Captura de asistencia geolocalizada mediante fotografía.

Registro de hora, ubicación y evidencia visual.

Envío inmediato si está conectado o almacenamiento local para envío posterior.

🧾 4. Enviar Evidencia de Factura
Captura o carga de imagen de factura entregada.

Relación con pedido correspondiente.

Subida al servidor con metadatos del pedido.

🎯 5. Ver Metas
Visualización de metas mensuales y su progreso.

Comparativas entre metas asignadas y objetivos logrados.

🕒 6. Línea de Tiempos
Cronología de actividades realizadas por día o semana.

Incluye check-ins, pedidos, visitas y registros de evidencia.

📍 7. Ubicación en Tiempo Real
Geolocalización de representantes en tiempo real mediante punteros en el mapa.

Visualización jerarquizada (supervisores pueden ver todos los representantes bajo su cargo).

🌐 Funcionalidad Online y Offline
Modo Offline: Todos los módulos permiten el trabajo sin conexión. La información se almacena localmente hasta que se detecta conexión.

Modo Online: Envío de datos en tiempo real al servidor.

Condición de red: Si no se encuentra en la red interna de Progela, se requiere conexión mediante VPN Cisco Security para acceso al servidor.

👨‍💼 Vista de Supervisores
Funcionalidades idénticas al representante.

Acceso adicional a:

Información consolidada de todos los representantes a su cargo.

Reportes de productividad.

Ubicación de cada representante.

Detalle de pedidos, asistencias y evidencias por persona.

⚙️ Tecnologías Utilizadas
Android SDK

Java

SQLite (almacenamiento local offline)

REST API (comunicación con servidor)

Retrofit/Volley (consumo de servicios web)

Google Maps API (mapas y geolocalización)

Firebase (opcional) para notificaciones push

VPN Cisco Secure Client (seguridad y acceso remoto)

🛠️ Estructura del Proyecto (MVC)
pgsql
Copiar
Editar
/src/
│
├── model/
│   └── Entidades (Prospecto, Pedido, Meta, etc.)
│
├── view/
│   └── Actividades, Fragments, Layouts XML
│
├── controller/
│   └── Lógica de negocio, controladores de datos, sincronización, adaptadores, etc.
📤 Sincronización de Datos
Mediante API RESTful.

Cola de sincronización en caso de modo offline.

Reintentos automáticos cuando se restablece conexión.

Notificación al usuario sobre estado de sincronización.

📈 Posibilidades de Expansión
Implementación de notificaciones push (Firebase).

Incorporación de firma digital en pedidos y facturas.

Panel web administrativo para monitoreo en tiempo real.

Análisis de datos en dashboards visuales.
