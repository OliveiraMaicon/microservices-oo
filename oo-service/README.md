# Olha O Rolé #
[![Codeship Status for thisisfuture/app-oor-backend](https://codeship.com/projects/c9cf3f60-b34a-0133-b9a0-1a30591bad79/status?branch=master)](https://codeship.com/projects/133667)

[![Build Status](https://semaphoreci.com/api/v1/projects/4f3cb684-e20d-4dae-b988-b7b1f9e4964d/695090/badge.svg)](https://semaphoreci.com/andrelugomes/app-oor-backend)


## Requisitos

* JDK 1.8 
* Postgres

## install

```console
sudo psql --username=postgres --password
```

```console
postgres=# CREATE DATABASE olha_database;
```

## Running API Service
```console
mvn clean install spring-boot:run
```

## Skiping Tests
```console
mvn clean install -DskipTests spring-boot:run
```