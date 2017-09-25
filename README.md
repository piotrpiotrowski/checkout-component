# Checkout Component

## Description
Checkout component is micro-service which is able to store customers orders.
In order to get customer data is communicating via REST API with customer micro-service.
All information regarding pricing of order item are retrieved via REST API from pricing micro-service.

## Used technologies
* Java 8
* RxJava
* Spring-Boot
* Hazelcast
* Spring Cloud Contract
* Wiremock

## Building
In order to build app run:
```bash
gradlew clean build install
```

## Running locally
In order to run app locally use IntelliJ Spring-Boot Configuration.
The easiest way to do that is click green triangle icon next to name of main class
``org.siemasoft.checkout.CheckoutApplication``.

## Local stubs
1. You need to download wiremock.jar by
```bash
curl https://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-standalone/2.8.0/wiremock-standalone-2.8.0.jar   
```
 
2. Run wiremock for customer service
```bash
java -jar wiremock-standalone-2.8.0.jar --port 9910 --verbose --root-dir customer/build/stubs/META-INF/org.siemasoft/customer/0.0.1-SNAPSHOT/
```
 
3. Run wiremock for pricing service
```bash
java -jar wiremock-standalone-2.8.0.jar --port 9920 --verbose --root-dir pricing/build/stubs/META-INF/org.siemasoft/pricing/0.0.1-SNAPSHOT/
```