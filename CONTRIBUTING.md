# Contributing

I don't expect anyone else to actually contribute to my personal site. But _I_ am a contributor, and sometimes I need a reminder too.

## Prerequisites

- Java (version matching the project's Maven configuration)

## Running Locally

The project includes a Maven wrapper, so use that to ensure compatibility. To start the app locally with an in-memory H2 database:

```
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
