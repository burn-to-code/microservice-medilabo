# microservice-medilabo
Application Micro Service

# 🧬 Microservice Medilabo

![Build](https://img.shields.io/badge/build-passing-brightgreen?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-blue?style=flat-square)
![Java](https://img.shields.io/badge/java-21-blue?style=flat-square)
![Spring Boot](https://img.shields.io/badge/spring_boot-3.5.6-brightgreen?style=flat-square)
![Docker](https://img.shields.io/badge/docker-ready-blue?style=flat-square)

> Application distribuée basée sur une architecture **microservices** (Spring Boot, Gateway, Eureka, Feign, Docker).


Application Micro Service


# 🧬 Microservice Medilabo

## 📑 Sommaire
- [1. Présentation générale](#1-présentation-générale)
    - [Objectif du projet](#-objectif-du-projet)
    - [Technologies principales](#-technologies-principales)
- [2. Architecture microservices](#2-architecture-microservices)
    - [Communication interservices](#-communication-interservices)
    - [Gateway](#-gateway)
- [3. Structure du projet](#3-structure-du-projet)
- [4. Détail par microservice](#-4-détail-par-microservice)
    - [Patient](#41--patient)
    - [Front](#42--front)
    - [Gateway](#43--gateway)
    - [Common](#44--common)
    - [Eureka](#45--eureka)
    - [Notes](#46--notes)
    - [Risk](#47--risk)
- [5. Sécurité](#-5-sécurité)
- [6. Stratégie de tests](#-6-stratégie-de-tests)
    - [Tests unitaires](#-tests-unitaires)
    - [Tests semi-intégration](#-tests-semi-intégration)
    - [Évolutions prévues](#-évolutions-prévues)
- [7. Détails techniques](#-7-détails-techniques)
- [8. Lancement du projet](#-8-lancement-du-projet)
- [9. Améliorations futures](#-9-améliorations-futures)
- [10. Auteur & Licence](#-10-auteur--licence)



##  1. Présentation générale
Medilabo est une application distribuée basée sur une architecture microservices permettant de gérer les patients d’un laboratoire médical.  
Elle se compose de plusieurs services indépendants communiquant via Spring Cloud Gateway et Feign Client.

### 🎯 Objectif du projet
- Centraliser les informations des patients (données personnelles, antécédents, etc.) et calculer leurs risques de diabètes en fonction de certains critères (âge, notes, etc.)
- Offrir une interface front simple pour la consultation et l’ajout de patients, de notes et de risques.
- Assurer une communication sécurisée et découplée entre les modules

### 🧰 Technologies principales
- Java 21
- Spring Boot 3.5.6
- Spring Cloud Gateway
- Spring MVC / Web
- Spring Security
- Feign Client
- MySQL 8
- MongoDb 6
- Thymeleaf
- Lombok, Jakarta Validation
- Docker & Docker Compose
- Maven 3.9+

##  2. Architecture microservices
Le projet repose sur 6 microservices + un module commun :

| Service | Port | Description                                                               |
|---------|------|---------------------------------------------------------------------------|
| patient | 8081 | Service métier responsable de la gestion des patients (MySql)             |
| gateway | 8082 | Point d’entrée unique de l’application, gère la sécurité et le routage    |
| front   | 8080 | Interface utilisateur (Thymeleaf)                                         |
| common  | —    | Contient les DTO, enums et classes partagées                              |
| eureka  | 8761 | Serveur de découverte des service (Eureka)                                |
| notes   | 8083 | Service métier responsable de la gestion des notes des patient (MongoDB)  |
| risk    | 8084 | Service métier responsable de la gestion du calcul des risques de diabète |


### 🔀 Communication interservices
- Tous les microservices (front, patient, gateway, notes, risk) sont désormais enregistrés dans Eureka pour permettre la découverte et le load-balancing côté Feign/Gateway.
- Les URLs ne pointent plus directement vers les services (http://gateway:8082) mais utilisent le serviceId Eureka (lb://gateway) via Spring Cloud LoadBalancer.
- Les appels internes utilisent Feign Client via Eureka.
- L'utilisateur passe par le front qui communique uniquement via gateway.

### 🔒 Gateway
- Fait office de reverse proxy
- Gère la sécurité, les filtres, et le routage
- Différencie les routes internes API des routes utilisateurs

##  3. Structure du projet

```text
microservice-medilabo/
│
│
├── .github/
│   ├── workflows/
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
├── notes/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── risk/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── common/
│   └── src/
│
├── Eureka/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml
└── pom.xml
└── qodana.yml

```

## 🧩 4. Détail par microservice

### 4.1 🩺 Patient
**Objectif :** gérer les données patients (CRUD complet)  
**Base de données :** MySQL 8

**Endpoints REST**
```html
> GET    /patients
> GET    /patients/{id}
> POST   /patients
> PUT    /patients/{id}
> DELETE /patients/{id}
```

**Exemple de payload JSON**
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "birthDate": "1995-05-05",
  "gender": "F",
  "address": "456 rue B",
  "phone": "0607080910"
}
```

### 4.2 🖥️ Front
**Objectif :** interface utilisateur via Thymeleaf
**Base de données :** uniquement via la Gateway

```html
> GET  /patient
> GET  /patient/add
> POST /patient/save
> GET  /patient/edit/{id}
> POST /patient/edit/{id}
```

**Vue Thymeleaf :**

- login.html
- patient/list.html
- patient/add.html
- patient/edit.html
- patient/history.html
- error.html

### 4.3 🌐 Gateway
**Rôle :**

- Routage et filtrage des requêtes
- Passage de Spring MVC + WebSecurity vers Spring WebFlux + WebFluxSecurity
- Application de la sécurité
- Gestion de la SecurityWebFilterChain :
  - Sécurité appliquée via BasicAuth + SecurityWebFilterChain
  - CSRF désactivé pour permettre les appels Feign POST/PUT depuis le front
- Spring Cloud Gateway

**Exemple de redirection** 

```bash
/patient → front (UI)
/patients → patient (API)
```

### 4.4 🧱 Common
**Contient les DTO, enums, et classes partagées.**  
**Exemples :** PatientDTO, Gender (Enum)

### 4.5 🌟 Eureka
**Objectif :** gestion de la découverte des microservices et du load-balancing  
**Port :** 8761

**Fonctionnalités :**
- Chaque microservice (`front`, `gateway`, `patient`) s’enregistre automatiquement sur Eureka au démarrage.
- Permet aux clients Feign et à la Gateway de résoudre dynamiquement les adresses des services via leur `serviceId`.
- Compatible avec Spring Cloud LoadBalancer pour équilibrer la charge si plusieurs instances sont présentes.

**Exemple :**
- Front → Gateway via `lb://gateway`
- Gateway → Patient via `lb://patient`

**Accès UI Eureka :**
```text
http://localhost:8761
```

### 4.6 📝 Notes
**Objectif :** gérer les notes patients en gardant mémoire id et leur nom
**Base de données :** MongoDB 6

**Endpoints REST**
```html
> GET  /notes/{id}
> POST /notes
```

**Exemple de payload JSON**

**Pour les réponses :**
```json
{
  "Notes": "Une note",
  "creationDate": "2023-12-03"
}
```


**Pour les requêtes :**
```json
{
  "patientId": "2",
  "patientName": "Doe",
  "note": "Une note"
}
```

### 4.7 ☣️ Risk
**Objectif :** Ajouter a un patientDTO sont risque de diabète en fonction de son âge et de ses notes. 
**Base de données :** Aucune, appelle uniquement les services Patient/Notes API.

**Fonctionnalités :**
- Renvoi un patient avec son risque de diabète. Peut renvoyer une liste de tous les patientDTO ou d'un PatientDTO via son id.
- Pour calculer le diabète de l'utilisateur, les données nécessaires sont l'âge et les notes.
- Si un service appellant à une erreur le service renvoie DonnéesInsuffisantes dans le DTO.
- le service utilise Contains pour compter les mots à risque. Pour cela, veillez à rajouter un mot uniquement au singulier et au masculin si possibles.
- Pour rajouter un mot à risque, il suffit de le mettre dans le fichier risk/src/main/java/com/microservice/risk/model/WordsFactorDiabetes.java.

**Niveau de risques possibles :**
- None
- BorderLine
- InDanger
- EarlyOnSet
- DonneesInsuffisante

**Endpoints REST**
```html
> GET /risk
> GET /risk/{id}
```

**Exemple de payload JSON**
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "birthDate": "1995-05-05",
  "gender": "F",
  "address": "456 rue B",
  "phone": "0607080910",
  "riskOfDiabetes": "None"
}
```

### 🔒 5. Sécurité
**niveaux de filtrage dans la Gateway :**
- Front → Gateway → Patient : tout passe par la Gateway, qui applique BasicAuth.
- Les appels Feign depuis le front vers la Gateway utilisent maintenant BasicAuth dynamique (header Authorization) fourni par AuthSession.
- Les requêtes non authentifiées renvoient 401 Unauthorized, permettant au front de gérer la redirection vers /login.

**Tests unitaires :**
- Mock du PatientService
- Utilisation de JUnit 5 et Mockito

**Tests d’intégration :**
- Basés sur MockMvc
- Validation des redirections, statuts HTTP et rendu des vues
- Exécution isolée sans dépendance réelle à la base


### 🔒 6. Stratégie de tests

L’objectif des tests est d’assurer la fiabilité du code, la cohérence des échanges entre services et la robustesse face aux cas d’erreur.

### 🔹 Tests unitaires
Chaque **microservice** contient des tests unitaires couvrant :
- Les **services** (logique métier et gestion des exceptions),
- Les **contrôleurs** (comportements attendus et statuts HTTP).

Les cas gérés incluent :
- Les scénarios **valides** (succès),
- Les scénarios **invalides** entraînant des **exceptions personnalisées**.

Ces tests utilisent principalement des mocks (`@Mock`, `@MockBean`) pour isoler la logique du service testé.

### 🔹 Tests semi-intégration
Des **tests semi-intégration** ont été ajoutés afin de valider :
- Le fonctionnement complet d’un microservice (**contrôleur → service → repository simulé**),
- Les principaux **cas valides** d’exécution.

Les autres microservices sont **mockés** dans ces tests afin de limiter la complexité et d’éviter les dépendances croisées.  
Cette approche permet de vérifier la cohérence interne d’un service sans avoir à déployer tout l’écosystème.

### 🔹 Évolutions prévues
- Mise en place de **tests d’intégration complets** avec un environnement de test regroupant plusieurs microservices.
- Utilisation possible de **Testcontainers** ou d’un **Docker Compose dédié** pour simuler les échanges inter-services.

### 🧰 7. Détails techniques
| Composant   | Version                                   |
|-------------|-------------------------------------------|
| Java        | 21                                        |
| Spring Boot | 3.5.6                                     |
| Maven       | 3.9+                                      |
| MySQL       | 8                                         |
| MongoDB     | 6                                         |
| Outils      | Lombok, Validation Jakarta, Feign, Docker |

### 🚀 8. Lancement du projet
**Via Docker :**
```bash
mvn clean install -DskipTests
docker-compose up --build
```

Les services seront accessibles sur :

- Tous les services doivent démarrer avec Eureka actif pour être découverts correctement : 
- **Eureka** → http://localhost:8761/
- **Gateway** → http://localhost:8082/
- **Front** → http://localhost:8080/patient
- **Patient API** → http://localhost:8081/patients
- **Notes API** → http://localhost:8083/notes
- **Risk API** → http://localhost:8084/risk

### Via Maven
- **Sur tous les services :**
```bash
mvn clean install
mvn spring-boot:run
```

### 🧾 9. Améliorations futures
- Authentification OAuth2 / JWT
- Ajout de Spring Boot Actuator
- Mise en place de GitHub Actions pour CI/CD
- Observabilité et traçabilité (logs, metrics)
- Travail sur le green code

### 📜 10. Auteur & Licence
- **Auteur :** Dylan Senasson
- **Licence :** Medilabo