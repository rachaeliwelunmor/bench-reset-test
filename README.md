# Daily Running Balance
Get transaction list from API transform the list and print result to console.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software

```
Java 11
```

## Running the tests

This project contains unit tests. You can run them using the command below

```
mvn test
```
## Running the application

You can run the application using the command below

```
mvn clean install -DskipTests=true && java -jar target/rest-test-cli-0.0.1-SNAPSHOT.jar
```

## Assumption

This application assumes that
1. The API will return a list of transactions if we get a 200 response.
