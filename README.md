---
Lenguaje: Scala
Framework: Play 2.5
Autor: Sebastián Flórez
---

# CRUD BookStore Scala Play Framework
El siguiente proyecto expone servicios REST para el manejo de recursos de un contexto de una tienda de libros. Se cuenta con las funcionalidades básicas de CRUD para las entidades Book, Author, Review y Editorial.

Cuenta además con integración con SonarQube para el seguimiento de métricas de calidad.

## Prerequisitos
Es necesario contar con las siguientes herramientas:
- [SBT](http://www.scala-sbt.org/index.html) 0.13.12 o superior.
- [Intellij Idea](https://www.jetbrains.com/idea/)
- Base de datos Postgres SQL.
- SonarQube 5.6.6

## Instrucciones de ejecución 

### Paso 1: Crear base de datos

Desde la linea de comando de PostgresSQL crear las siguientes bases de datos:

1. Base de datos de ejecución.
```
CREATE DATABASE bookstore;
```

2. Base de datos de pruebas.
```
CREATE DATABASE bookstore_test;
```

## Paso 2: Modificar referencias a base de datos del proyecto.

En el archivo database.conf ubicado en la ruta conf/ ingresar los datos correspondientes a la base de datos creada.

```
Database = {
  url = "jdbc:postgresql://localhost:5432/bookstore"
  driver = org.postgresql.Driver
  connectionPool = disabled
}
```

En el archivo database.test.conf ubicado en la ruta conf/ ingresar los datos correspondientes a la base de datos de prueba creada.

```
Database = {
  url = "jdbc:postgresql://localhost:5432/bookstore_test"
  driver = org.postgresql.Driver
  connectionPool = disabled
}
```

## Paso 3: Importar en Intellij IDE
1. Abrir Intellij Idea.
2. Seleccionar "Import Project".
3. Seleccionar el directorio de la solución.
4. Seleccionar la opción "Importar de modelo externo" y seleccionar "SBT".
5. Validar que se cuenta con un "SDK de ejecución".
6. Finalizar.

## Paso 4: Agregar configuraciones de ejecución
Para crear una configuración en Intellij, seleccionar la opción "Edit Configurations" ubicada en la parte superior derecha.

### Crear configuración de ejecución
1. Seleccionar la opción "+" para agregar una nueva configuración
2. Seleccionar "Play 2 App"
3. Agregar un nombre significativo. Por ejemplo: Start App
4. Finalizar.

### Crear configuración de pruebas
1. Seleccionar la opción "+" para agregar una nueva configuración
2. Seleccionar "ScalaTest"
3. Agregar un nombre significativo. Por ejemplo: Start Test.
4. Agregar en VM Parameters.
```
-Dconfig.file=conf/application.conf -Dconfig.file=conf/database.test.conf
```
Esta sentencia indica a Scalatest la referencia de los archivos de configuración.

5. Verificar que se tengan las siguientes opciones seleccionadas
- "Test Kind" -> "All in package"
- "Search for tests" ->"Across Module Dependencies"
- "Use classpath and SDK module" -> "bookstore"
6. Finalizar.

## Paso 5: Ejecutar
Seleccionar la configuración a correr y dar click en el boton de ejecución (botón play).

Para el caso de ejecución de la aplicación se despliega la siguiente ventan en el navegador.

Para el caso de pruebas se muestra la siguiente ventana en Intellij

## Paso 6: Ejecutar en SonarQube
1. Agregar los archivos .jar presentes en el directorio *sonar-plugins* en el directorio de sonar *[ruta-sonar]/extensions/plugins*.

2. Desde linea de comandos ejecutar los siguientes procedimientos para cargar las métricas de la solución en sonar. Verificar la configuración del archivo sonar-project.properties:

```
sbt coverage test -Dconfig.file=conf/application.conf -Dconfig.file=conf/database.test.conf 
```

```
sbt coverageReport sonar
```
Nota: Es importante que se ejecuten de forma separada.

## [Opcional]: Ejecutar desde linea de comandos
### Ejecutar aplicación
Ejecutar el siguiente comando desde el proyecto raíz

```
sbt run
```

### Ejecutar pruebas
Ejecutar el siguiente comando desde el proyecto raíz

```
sbt test -Dconfig.file=conf/application.conf -Dconfig.file=conf/database.test.conf 
```