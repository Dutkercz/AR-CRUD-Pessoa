#Trazendoc um maven pro projeto
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

#Copiar PATH "src" PARA DENTRO DO CONTAINER NO PATH "/app/src" (copimos nossos arquivos de codigo para dentro do container)
COPY src /app/src

#Copiar ARQUIVO "pom.xml" PARA DENTRO DO CONTAINER NO PATH "/app" (copiamos o pom.xml da nossa aplicação)
COPY pom.xml /app

#Aqui estamos entrado dentro desse diretorio da aplicação
WORKDIR /app

#Uma vez dentro do diretorio, vamos fazer a instalação das dependencias e build da aplicação com o Maven que criamos lá em cima
#Geramos o .jar com as dependencias instaladas com o comando
RUN mvn clean install

#Pegamos uma imagem jdk Java
FROM eclipse-temurin:21-jre-alpine

#Agora copiar o arquivos .jar pra dentro /app/jar
COPY --from=build /app/target/db-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
