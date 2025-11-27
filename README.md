Estudiantes

- Carlos Bastias
- Matias Belmar

Descripción del Proyecto

RecetApp es una aplicación que permite explorar recetas organizadas por categorías como tipo de comida, estilo culinario y ocasión. 
Cada receta incluye imagen, descripción, ingredientes y pasos. 
Los usuarios pueden iniciar sesión, guardar recetas como favoritas y disfrutar de una interfaz moderna con animaciones. 
La información se guarda localmente usando Room, y se integran recursos nativos del dispositivo para mejorar la experiencia.

Funcionalidades Implementadas

-  Interfaz visual con navegación clara entre pantallas
-  alidación de formularios (correo y contraseña con regex)
-  Animaciones en listas de recetas (`LazyColumn` con `animateItemPlacement`)
-  Arquitectura modular con MVVM
-  Persistencia local con Room (entidad `Receta`)
-  Login y registro con validación contra base de datos
-  Gestión de favoritos persistente por usuario

Pasos para Ejecutar el Proyecto

Clonar el repositorio:

   ```bash
   git clone https://github.com/tu_usuario/RecetApp.git
