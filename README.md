# e-commerce-prototype
Ekaterina Ilina (lukashevaekaterina64@mail.ru)

### Overview
E-COMMERCE-PROTOTYPE is an e-commerce system which consists of various marketplaces. Users can choose an item and make an order, app contains full information about user-store communication, providers and deliveries
* commerce-core: The main module, which contains the main logic of the project.
* kafka-producer: Creates new orders and send them to Kafka's topics. Commerce-core contains Kafka Listener, listens and saves new orders to database.
* ai-recommendation-helper: A recommendation system based on GigaChat used to stimulate additional sales. Recommends products to users based on their previous purchases.

### Features
* Stores data in PostgreSQL.
* Configuring application logging and uploading metrics using Prometheus.
* Generates new orders using Apache Kafka.
* Makes a list of recommendations using GigaChat.
* Uses HTTP and GRPC protocols.
* Caching systems - Caffeine and Redis.