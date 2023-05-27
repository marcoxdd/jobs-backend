# Define a imagem base a ser utilizada
FROM openjdk:11-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR da aplicação para o contêiner
COPY target/api-0.0.1-SNAPSHOT.jar .

# Define o comando a ser executado quando o contêiner for iniciado
CMD ["java", "-jar", "my-application.jar"]