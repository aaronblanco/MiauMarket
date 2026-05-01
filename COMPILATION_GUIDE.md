# Guía de Solución: Compilación del Módulo core-data

## Problema Actual

```
Error resolving plugin [id: 'com.android.library', version: '9.1.1']
> The request for this plugin could not be satisfied because the plugin is already on 
  the classpath with an unknown version, so compatibility cannot be checked.
```

## Causa

El plugin `android.library` está siendo resuelto de múltiples formas:
1. Desde pluginManagement de settings.gradle.kts
2. Desde alias en libs.versions.toml
3. Ya cargado por el módulo `:app` 

Esto causa un conflicto de versionado.

## Soluciones (elige una)

### Solución 1: Usar build-logic (RECOMENDADO PARA PROYECTOS GRANDES)

Crear módulo build-logic:
```kotlin
// build-logic/build.gradle.kts
plugins {
    `kotlin-dsl`
}

// build-logic/src/main/kotlin/miaumarket.library.gradle.kts
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 35
}

// build-logic/src/main/kotlin/miaumarket.library.serialization.gradle.kts
plugins {
    id("miaumarket.library")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}
```

Luego en core-data/build.gradle.kts:
```kotlin
plugins {
    id("miaumarket.library.serialization")
}
```

### Solución 2: Usar IDs Directos sin Versión (RÁPIDO)

En core-data/build.gradle.kts:
```kotlin
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    // Nota: serialization plugin NO se aplica directamente
    // Se maneja como dependencia
}

// En la carga de proyectos, aplicar serialización a nivel de build.gradle.kts:
// (Sin usar plugins {}, simplemente en scripts de apply)
```

### Solución 3: Separar en dos módulos

```
core-data/
├── core-data-api/           (sin Kotlin, solo DTOs)
└── core-data-impl/          (implementación con plugins)
```

### Solución 4: Usar Convention Plugins Simples

En settings.gradle.kts:
```kotlin
pluginManagement {
    includeBuild("build-logic")
    // ...
}
```

## RECOMENDACIÓN INMEDIATA

Usa **Solución 2** para arreglarlo rápido:

1. Abre `core-data/build.gradle.kts`
2. Cambia a:
```kotlin
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    // ... resto del config igual
}

// En lugar de usar kotlin.serialization, agrega la dependencia directamente
// en lugar del plugin
```

3. En las dependencias, agrega:
```kotlin
dependencies {
    // ... otras deps
    
    // Serialization (como dependencia, no plugin)
    implementation(libs.kotlinx.serialization.json)
}
```

4. Ejecuta:
```bash
./gradlew build
```

## Verificación

Una vez compilado exitosamente:
```bash
./gradlew clean build --refresh-dependencies
./gradlew :wear:assembleDebug
./gradlew :app:assembleDebug
```

## Próximos Pasos

1. **Compilar módulos**
   ```bash
   ./gradlew :core-data:build
   ./gradlew :app:build
   ./gradlew :wear:build
   ```

2. **Instalar en emulador**
   ```bash
   # App móvil
   ./gradlew :app:installDebug
   
   # Wear
   ./gradlew :wear:installDebug
   ```

3. **Testing en emulador**
   - Abrir app móvil
   - Login con credentials
   - Abrir app Wear en emulador
   - Verificar que catálogo se carga

## En caso de Persistencia del Error

Si el problema persiste después de aplicar la Solución 2:

```bash
# 1. Clear caches
rm -rf .gradle
rm -rf build/
rm -rf core-data/build/
rm -rf app/build/
rm -rf wear/build/

# 2. Clean Android Studio caches (File → Invalidate Caches)

# 3. Try rebuild
./gradlew clean build
```

## Contacto para Dudas

Revisa el archivo `WEAR_OS_IMPLEMENTATION.md` para más contexto técnico.

