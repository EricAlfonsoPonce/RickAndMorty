# Rick & Morty - Android Clean Architecture Showcase

🚀 **Professional Android application demonstrating Clean Architecture, Jetpack Compose, and best engineering practices.**

This project serves as a practical showcase of a production-ready architecture, focusing on scalability, modularization, and the interoperability between legacy (XML) and modern (Compose) UI toolkits.

## 🛠 Tech Stack & Libraries

* **Language:** Kotlin (100%)
* **Architecture:** Clean Architecture (Multi-module: `:app`, `:domain`, `:data`, `:di`, `:presentation`)
* **Design Pattern:** MVVM (Model-View-ViewModel) + MVI concepts
* **UI:** Jetpack Compose & XML (Interoperability demonstration)
* **Dependency Injection:** Dagger Hilt
* **Concurrency:** Coroutines & Flow
* **Network:** Retrofit2 & Gson
* **Local Data:** Room Database
* **Testing:** JUnit 4, Mockk, Turbine, Coroutines Test

## 🏗 Architecture Overview

The project follows strict **Clean Architecture** principles to ensure separation of concerns and testability:

1.  **Domain Layer:** Pure Kotlin module. Contains Entities, Use Cases, and Repository Interfaces. No Android dependencies.
2.  **Data Layer:** Handles data retrieval (API/DB). Implements Repositories and maps DTOs to Domain Entities.
3.  **Presentation Layer:** Contains UI logic (Activities, ViewModels, Composables).
4.  **DI Layer:** Manages dependency injection graph using Hilt.

## ✨ Key Features

* **Clean Architecture & SOLID Principles:** Strictly decoupled layers.
* **Hybrid UI Strategy:** Seamless integration of Jetpack Compose within an existing XML-based navigation structure, simulating real-world migration scenarios.
* **Robust Error Handling:** Unified error management for Network and Database exceptions.
* **Unit Testing:** Comprehensive testing for ViewModels and UseCases using Mockk and Turbine.
* **Pagination:** Custom pagination logic for character lists.
