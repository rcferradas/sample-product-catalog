# sample-product-catalog
This is a sample project for a product catalog service. 
This service provides an API Rest for retrieving a list of products.

## Requirements
JDK 17
Kotlin 1.9
Gradle 8.12.1+

### Startup
To build the application: ```./gradlew build```

To run the application on local env: ```./gradlew bootRun```

To run tests on local env: ```./gradlew test```

### Architecture Explanation
Following the DDD concepts, domain and service packages hold the main business logic and entities 
while infrastructure package contains database logic and rest package have presentation logic.
In order to separate the business logic from both implementations of database and rest layers:

ProductService interface separates the domain logic from the rest layer.

ProductRepository abstracts the domain from the underlying database infrastructure

This approach allows the core business logic to be independent of the chosen rest or database 
implementations, which results in low coupling and high cohesion.