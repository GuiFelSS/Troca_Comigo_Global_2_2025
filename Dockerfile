# Estágio 1: Build (Construção)
# Usamos uma imagem Maven com Java 17 para compilar o projeto
FROM maven:3.9-eclipse-temurin-17 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia o pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .

# Baixa as dependências (sem compilar o código ainda)
RUN mvn dependency:go-offline

# Copia o restante do código-fonte
COPY src ./src

# Compila o projeto e empacota o .jar
RUN mvn package -DskipTests

# Estágio 2: Run (Execução)
# Usamos uma imagem JRE (Java Runtime) limpa e leve
FROM eclipse-temurin:17-jre-focal

# Define o diretório de trabalho
WORKDIR /app

# Copia o .jar construído do estágio anterior
# O nome do artefato (globalSolution-0.0.1-SNAPSHOT.jar) vem do seu pom.xml
COPY --from=builder /app/target/globalSolution-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que o Spring Boot usa (padrão 8080)
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]