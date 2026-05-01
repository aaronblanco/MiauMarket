# Implementación Wear OS para MiauMarket

## Resumen de la Implementación

Se ha realizado la creación de una aplicación básica para Galaxy Watch 6 que muestra el catálogo de MiauMarket.

### Estructura del Proyecto

```
MiauMarketv2/
├── app/                    # Aplicación móvil (Android Phone)
├── core-data/              # Módulo compartido con lógica de datos
│   ├── src/main/java/com/example/miaumarket/core/data/
│   │   ├── di/                    # Inyección de dependencias
│   │   ├── domain/                # Interfaces de repositorio
│   │   ├── local/                 # DataStore, SessionManager
│   │   ├── remote/                # APIs Retrofit, DTOs, Interceptor
│   │   └── repository/            # Implementaciones de repositorio
│   └── build.gradle.kts
└── wear/                   # Aplicación Wear OS
    ├── src/main/java/com/example/miaumarket/wear/
    │   ├── MainActivity.kt
    │   ├── ui/
    │   │   ├── navigation/         # Navegación con Wear Compose
    │   │   ├── screens/
    │   │   │   ├── catalog/       # CatalogScreen, CatalogViewModel
    │   │   │   └── product/       # ProductDetailScreen, ProductDetailViewModel
    │   │   └── theme/              # Temas para Wear OS
    │   └── MiauMarketApp.kt
    └── build.gradle.kts
```

## Módulo core-data

### Características
- **DTOs (Data Transfer Objects)**: ProductResponse, AuthResponse, UserResponse, etc.
- **APIs Retrofit**: ProductApi, AuthApi para llamadas REST
- **Repositorios**: ProductRepository, AuthRepository, CartRepository (interfaces y implementaciones)
- **SessionManager**: Gestión de token JWT con DataStore
- **AuthInterceptor**: Injección automática del token en los headers de Retrofit

### Dependencias Clave
- Retrofit + Moshi: Para llamadas a API
- DataStore: Almacenamiento seguro de tokens
- Hilt: Inyección de dependencias
- KSP: Compilador de anotaciones

## Módulo Wear OS

### Pantallas Implementadas

#### 1. CatalogScreen
- Muestra lista paginada de productos
- Soporte para cargar más productos
- Manejo de errores y estados de carga
- Adaptado para pantalla circular/pequeña

#### 2. ProductDetailScreen
- Detalles completos del producto
- Botón "Añadir al carrito"
- Botón "Atrás" para navegar
- Información de origen y fecha de actualización

### ViewModels
- **CatalogViewModel**: Gestiona la paginación y carga de productos
- **ProductDetailViewModel**: Obtiene detalles del producto y maneja el carrito

### Navegación
Usa Wear Compose Navigation con `SwipeDismissableNavHost` para:
- Navegar entre pantallas con swipe
- Pasar parámetros de navegación (ID del producto)
- Manejar backstack

### Tema
- Colores optimizados para Wear OS
- Tipografía adecuada para lectura en reloj
- Tema oscuro por defecto (GameTheme.DeviceDefault)

## Archivos Modificados

### app/
- `build.gradle.kts`: Agregada dependencia `implementation(project(":core-data"))`
- `di/RepositoryModule.kt`: Actualizado para usar imports de core-data
- `di/NetworkModule.kt`: Marcado como obsoleto (los módulos ahora vienen desde core-data)
- ViewModels:
  - `LoginViewModel.kt`
  - `RegisterViewModel.kt`
  - `ProductViewModel.kt`
  - `CartViewModel.kt`

### gradle/
- `libs.versions.toml`: Agregadas versiones para dependencias de Wear
  - `wearCompose = "1.3.1"`
  - `wearComposeNavigation = "1.3.1"`
  - Agregado entry de `android-library` plugin

### settings.gradle.kts
- Agregados módulos `:core-data` y `:wear`

## Dependencias Compartidas

### core-data proporciona:
```kotlin
- RecoverableException (manejo de errores)
- SessionManager (manejo de sesión)
- ProductRepository, AuthRepository, CartRepository
- DTOs (ProductResponse, ProductRequest, AuthResponse, etc.)
- NetworkModule, RepositoryModule (Hilt configuration)
```

### app consume:
- Toda la capa de datos desde core-data
- Mantiene sus propias ViewModels y UI

### wear consume:
- Toda la capa de datos desde core-data
- Propias ViewModels y UI optimizada para Wear
- Navegación con Wear Compose

## Flujo de Autenticación

1. **App móvil**: Usuario se autentica
2. **Token guardado**: En SessionManager (DataStore) del core-data
3. **Wear accede al token**: SessionManager es inyectado vía Hilt
4. **AuthInterceptor**: Automáticamente agrega el token a todas las requests

### Para futuro - Data Layer Sync:
Se puede implementar la sincronización del token desde Phone a Wear usando:
```kotlin
com.google.android.gms:play-services-wearable:18.1.0
```

## Próximos Pasos (MVP)

### Fase 1: Compilación
- [ ] Resolver conflicto de plugins en core-data
  - Solución: Usar ID directo sin alias o configurar pluginManagement especial

### Fase 2: Funcionalidad Básica
- [ ] Cargar catálogo en Wear
- [ ] Ver detalles del producto
- [ ] Añadir a carrito (local)
- [ ] Persistencia de carrito

### Fase 3: Autenticación
- [ ] Sincronizar token desde app a reloj (Data Layer)
- [ ] Validar sesión al abrir Wear
- [ ] Logout en Wear

### Fase 4: Integraciones
- [ ] Notificaciones cuando hay nuevos productos
- [ ] Carrito sincronizado entre phone y watch
- [ ] Compartir datos entre aplicaciones

### Fase 5: Polish
- [ ] Optimizar imágenes para Wear (menor resolución)
- [ ] Proguard/R8 para reducir APK
- [ ] Pruebas en dispositivo real

## Problemas Conocidos & Soluciones

### Problema: "Plugin is already on the classpath with unknown version"
**Causa**: Conflicto entre versiones del plugin android.library
**Solución Propuesta**: 
- Usar IDs de plugin directo sin alias en core-data
- O configurar pluginManagement global con todas las versones

### Problema: Tamaño del APK
**Solución**: 
- Usar Proguard/R8
- Modularizar y descargar dinámicamente
- Remover dependencias innecesarias (Coil → coil-light)

## Conceptos de Wear OS

### Diferencias con Phone
1. **Pantalla circular/pequeña**: Máx 300x300 dp
2. **Interacción**: Swipe, botones grandes, gestos
3. **Fuente**: Texto más grande (para visibilidad)
4. **Conectividad**: Puede depender del teléfono
5. **Batería**: Eficiencia energética crítica

### Composables de Wear
- `ScalingLazyColumn`: Para listas optimizadas en Wear
- `Button`: Botones táctiles con buena ergonomía
- `SwipeDismissableNavHost`: Navegación con swipe
- `Chip`: Componentes compactos

## Testing

### Emulador Wear OS
```bash
# En Android Studio:
# Tools → SDK Manager → Wear OS images
# AVD Manager → Create Virtual Device (Wear OS)
# Ejecutar: ./gradlew :wear:installDebug
```

### Dispositivo Real
```bash
# Galaxy Watch 6:
# 1. Habilitar Developer Mode (tapping version number)
# 2. adb connect <watch_ip>:5555
# 3. ./gradlew :wear:installDebug
```

## Referencias
- [Wear OS Documentation](https://developer.android.com/wear)
- [Compose for Wear](https://developer.android.com/training/wearables/compose)
- [Galaxy Watch6 Specs](https://www.samsung.com/es/wearables/galaxy-watch6/)

