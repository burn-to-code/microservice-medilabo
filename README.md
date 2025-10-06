# microservice-medilabo
Application Micro Service

🧬 Microservice Medilabo
🏷️ 1. Présentation générale

Medilabo est une application distribuée basée sur une architecture microservices permettant de gérer les patients d’un laboratoire médical.
Elle se compose de plusieurs services indépendants communiquant via Spring Cloud Gateway et Feign Client.

🎯 Objectif du projet

Centraliser les informations patients (données personnelles, antécédents, etc.) et calculé leurs risques de diabètes en fonction du certains critères (âge, note, etc.) 

Offrir une interface front simple pour la consultation et l’ajout de patients, de note et de risque.

Assurer une communication sécurisée et découplée entre les modules

🧰 Technologies principales

Java 21

Spring Boot 3.5.6

Spring Cloud Gateway

Spring MVC / Web

Spring Security

Feign Client

MySQL 8

Thymeleaf

Lombok, Jakarta Validation

Docker & Docker Compose

Maven 3.9+

⚙️ 2. Architecture microservices

Le projet repose sur 4 microservices + un module commun :

Service	Port	Description
patient	8081	Service métier responsable de la gestion des patients
gateway	8080	Point d’entrée unique de l’application, gère la sécurité et le routage
front	8082	Interface utilisateur (Thymeleaf)
common	—	Contient les DTO, enums et classes partagées
🔀 Communication interservices

Le front communique uniquement avec la gateway

La gateway redirige les requêtes vers patient

Les appels internes utilisent Feign Client

Toutes les API sont accessibles via un préfixe /medilabo/...

🔒 Gateway

Fait office de reverse proxy

Gère la sécurité, les filtres, et le routage

Différencie les routes internes API des routes utilisateurs

🗂️ 3. Structure du projet
microservice-medilabo/
│
├── patient/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── front/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── gateway/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── common/
│   └── src/
│
├── docker-compose.yml
└── pom.xml

🧩 4. Détail par microservice
4.1 🩺 Patient

Objectif : gérer les données patients (CRUD complet).
Base de données : MySQL 8.

Endpoints REST
GET    /patients
GET    /patients/{id}
POST   /patients
PUT    /patients/{id}
DELETE /patients/{id}

Exemple de payload JSON
{
"firstName": "Jane",
"lastName": "Doe",
"birthDate": "1995-05-05",
"gender": "F",
"address": "456 rue B",
"phone": "0607080910"
}

4.2 🖥️ Front

Objectif : interface utilisateur via Thymeleaf
Communication : uniquement via la Gateway

Endpoints contrôleur
GET    /patient
GET    /patient/add
POST   /patient/save
GET    /patient/edit/{id}
POST   /patient/edit/{id}

Vues Thymeleaf

patient/list.html

patient/add.html

patient/edit.html

4.3 🌐 Gateway

Rôle :

Routage et filtrage des requêtes

Application de la sécurité

Gestion des deux SecurityFilterChain :

Filtrage API interne (JWT / header sécurisé)

Filtrage utilisateur (FormLogin)

Exemple de redirection
/medilabo/patient → front (UI)
/patients → patient (API)

4.4 🧱 Common

Contient les DTO, enums, et classes partagées

Exemples :

PatientDTO

Gender (Enum)

🔒 5. Sécurité

Deux niveaux de filtrage dans la Gateway :

FilterChain interne → protège les API (/patients, /internal/...)

FilterChain utilisateur → gère la session et l’authentification via formLogin

Chaque requête front passe obligatoirement par la Gateway, qui valide l’accès avant redirection vers le service cible.

🧪 6. Stratégie de test
Tests unitaires

Mock du PatientService

Utilisation de JUnit 5 et Mockito

Tests d’intégration

Basés sur MockMvc

Validation des redirections, statuts HTTP et rendu des vues

Exécution isolée sans dépendance réelle à la base

🧰 7. Détails techniques
Composant	Version
Java	21
Spring Boot	3.5.6
Maven	3.9+
MySQL	8
Outils	Lombok, Validation Jakarta, Feign, Docker
🚀 8. Lancement du projet
Via Docker
docker-compose up --build


Les services seront accessibles sur :

Gateway → http://localhost:8080

Front → http://localhost:8082/medilabo/patient

Patient API → http://localhost:8081/patients

Via Maven
mvn clean install
mvn spring-boot:run

🧾 9. Améliorations futures

Authentification OAuth2 / JWT

Ajout de Spring Boot Actuator

Mise en place de GitHub Actions pour CI/CD

Observabilité et traçabilité (logs, metrics)

📜 10. Auteur & Licence

Auteur : Dylan
Licence : MIT