FROM maven:3-amazoncorretto-17-alpine AS build

WORKDIR /app

# Crear usuario sin privilegios
RUN addgroup -S nonroot && adduser -S nonroot -G nonroot

# Como root, copia los archivos y establece permisos
USER root

COPY pom.xml ./
COPY src ./src

# Establece los permisos correctos
RUN chown -R nonroot:nonroot /app && \
    chmod -R 755 /app

# Cambia al usuario sin privilegios
USER nonroot


RUN mvn clean package -Pprod -DskipTests

FROM amazoncorretto:17-alpine AS runtime

WORKDIR /app

# Crear usuario no root para nginx
RUN addgroup -S nonroot && adduser -S nonroot -G nonroot

# Copiar archivos y establecer permisos
COPY --from=build /app/target/*.jar ./app.jar
# Establece los permisos correctos
RUN chown -R nonroot:nonroot /app && \
    chmod -R 755 /app

# Cambiar a usuario no root
USER nonroot


CMD ["java", "-jar", "app.jar"]