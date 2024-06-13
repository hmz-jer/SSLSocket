# Projet de Communication Socket SSL

Ce projet contient deux applications Java : un serveur (`ServerSocket`) et un client (`Client`). Les deux utilisent des sockets sécurisés (SSL) pour communiquer. Le projet démontre comment établir une connexion sécurisée entre un client et un serveur en utilisant des certificats PKCS12.

## Structure du Projet
```
/project-root
/src
/main
/java
ServerSocket.java
Client.java
/resources
server-keystore.p12
server-truststore.p12
client-keystore.p12
client-truststore.p12
build.gradle 
```

## Génération des Certificats

Pour générer les certificats nécessaires, suivez les commandes ci-dessous :

### Génération du keystore pour le serveur (ServerSocket)

```bash
keytool -genkeypair -alias serversocket -keyalg RSA -keysize 2048 -keystore server-keystore.p12 -storetype PKCS12 -validity 365
```

### Génération du keystore pour le client (Client)

```bash
keytool -genkeypair -alias client -keyalg RSA -keysize 2048 -keystore client-keystore.p12 -storetype PKCS12 -validity 365
```

### Exporter le certificat du serveur

```bash
keytool -exportcert -alias serversocket -keystore server-keystore.p12 -file server-cert.pem -rfc
```

### Importer le certificat du serveur dans le truststore du client

```bash
keytool -importcert -alias serversocket -file server-cert.pem -keystore client-truststore.p12 -storetype PKCS12
```

## Exporter le certificat du client

```bash
keytool -exportcert -alias client -keystore client-keystore.p12 -file client-cert.pem -rfc
```

### Importer le certificat du client dans le truststore du serveur

```bash
keytool -importcert -alias client -file client-cert.pem -keystore server-truststore.p12 -storetype PKCS12
```

### Compilation et Exécution

Assurez-vous que votre fichier build.gradle est configuré correctement.

Pour compiler et exécuter le serveur :

```bash

gradle build
gradle run
```

Pour compiler et exécuter le client, modifiez mainClassName dans build.gradle :

```
mainClassName = 'Client'
```

Puis exécutez :

```bash
gradle run
```

Pour compiler et exécuter le serveur :

```bash

gradle build
gradle run
```

Pour compiler et exécuter le client :
```bash

mainClassName = 'Client'
```

```bash
gradle run
```

## Vérification de la Connexion SSL
Utilisation de OpenSSL

Pour vérifier que la connexion SSL est bien établie, vous pouvez utiliser OpenSSL :

```bash

openssl s_client -connect localhost:12345 -CAfile server-cert.pem
```

## Journalisation dans le Code

Le code contient des instructions pour afficher des messages de journal indiquant que la connexion SSL a été établie.

    Serveur : Affiche "SSL connection established with client" lorsque la connexion est établie.
    Client : Affiche "SSL connection established with server" lorsque la connexion est établie.

## Fonctionnement

    Le serveur attend des connexions SSL sécurisées et écho les messages reçus.
    Le client envoie des messages au serveur et affiche les réponses du serveur.

Pour quitter le client, tapez exit et appuyez sur Enter.
