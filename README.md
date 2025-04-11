# e-commerce-prototype
Ekaterina Ilina (lukashevaekaterina64@mail.ru)

### Overview
E-COMMERCE-PROTOTYPE is an e-commerce system which consists of various marketplaces. Users can choose an item and make an order, app contains full information about user-store communication, providers and deliveries
* commerce-core: The main module, which contains the main logic of the project.
* kafka-producer: Creates new orders and send them to Kafka's topics. Commerce-core contains Kafka Listener, listens and saves new orders to database.
* ai-recommendation-helper: A recommendation system based on GigaChat used to stimulate additional sales. Recommends products to users based on their previous purchases.

### Features
* Stores order detailes in PostgreSQL.
* Configuring metrics calculation using Prometheus.
* Emulates order creation using Apache Kafka.
* Makes a list of recommendations using GigaChat.
* Uses HTTP (orders API, recommendations, authentication) and GRPC (bidirectional streaming for recommendaion service) protocols.
* Caching implementations - Caffeine and Redis.
* Uses CriteriaAPI to find most popular orders over a given time period with several optional parameters.
