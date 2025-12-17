# Smart Parking — Java (MVC) Final Project

This repository contains a compact Smart Parking System implemented in Java using an MVC-style package layout. It is designed for a final university exam project: easy to explain, testable, and extensible to a web application and persistence layer.

**No external dependencies** — uses only plain Java (no Maven, no JUnit).

**Project Goals**
- Demonstrate Object-Oriented design and MVC architecture.
- Provide driver and admin flows: search, reserve, occupy, release, and manage slots.
- Clean, well-documented Java code with simple assertions for testing.

**Project Structure**
- `src/main/java/com/example/smartparking/model`: Domain models (`User`, `ParkingSlot`, `Reservation`, `ReservationStatus`).
- `src/main/java/com/example/smartparking/service`: Business logic (`ParkingService`).
- `src/main/java/com/example/smartparking/controller`: Demo controller (`Main`) to show program flows.
- `src/test/java`: Test runner with plain Java assertions.

Getting started
---------------
Requirements:
- Java 8 or later (JDK)

Build and run from the project root using the PowerShell build script.

Windows PowerShell examples:
```powershell
# 1) Run demo (compile + execute)
.\build.ps1 run

# 2) Compile only
.\build.ps1 build

# 3) Run tests
.\build.ps1 test

# 4) Clean output directory
.\build.ps1 clean
```

Manual javac/java commands (if you prefer):
```powershell
# Compile main source
javac -d out\classes -encoding UTF-8 src\main\java\com\example\smartparking\**\*.java

# Run demo
java -cp out\classes com.example.smartparking.controller.Main

# Compile tests
javac -d out\test-classes -cp out\classes -encoding UTF-8 src\test\java\com\example\smartparking\**\*.java

# Run tests
java -cp out\test-classes;out\classes com.example.smartparking.service.ParkingServiceTest
```

Key classes (explainable in an exam)
------------------------------------
- `model.User` — Represents a driver with immutable `id`, `name`, and `contact`.
- `model.ParkingSlot` — Single parking slot with `id`, `location`, and state flags: `occupied`, `reserved`.
- `model.Reservation` — Binds a `User` to a `ParkingSlot`, records timestamps and `ReservationStatus`.
- `model.ReservationStatus` — Simple lifecycle enum: `RESERVED`, `ACTIVE`, `COMPLETED`, `CANCELLED`.
- `service.ParkingService` — Central business logic. Stores slots and reservations in-memory (`ConcurrentHashMap`). Synchronized methods ensure correctness for reserve/occupy/release flows. Contains admin helpers `addSlot` and `removeSlot`.
- `controller.Main` — Small console demo showing search → reserve → occupy → release flow; useful to step through during an oral exam.

Design notes & extensibility
---------------------------
- MVC separation: models are pure POJOs, business logic is in the service layer, and the `Main` class acts as a controller/view for demonstrations. Replacing `Main` with a REST controller or GUI is straightforward.
- Persistence: storage is currently in-memory. To persist data, add a DAO/repository layer and replace the in-memory maps in `ParkingService` with repository calls (JDBC, or any storage).
- Concurrency: service methods are `synchronized` for simplicity. In production you would use finer-grained locking or rely on transactional guarantees from a database.
- Features you can add easily: slot sizes, pricing & billing, multi-level garages, reservation expiration, or authentication for admin/user roles.

Exam talking points
-------------------
- Why MVC: decoupling of model, view/controller, and service simplifies testing and extension.
- Thread-safety choices: `ConcurrentHashMap` for concurrent reads and `synchronized` writes to keep logic simple and clear for an exam.
- No external libraries: pure Java demonstrates understanding of core concepts without framework noise.

Notes
-----
- The project uses minimal code and no external dependencies so students can focus on design and behavior.
- The code is intentionally short and well-commented to help explanation in a time-limited exam setting.
- All compilation and execution use standard `javac` and `java` commands (Windows PowerShell script provided for convenience).


Key classes (explainable in an exam)
------------------------------------
- `model.User` — Represents a driver with immutable `id`, `name`, and `contact`.
- `model.ParkingSlot` — Single parking slot with `id`, `location`, and state flags: `occupied`, `reserved`.
- `model.Reservation` — Binds a `User` to a `ParkingSlot`, records timestamps and `ReservationStatus`.
- `model.ReservationStatus` — Simple lifecycle enum: `RESERVED`, `ACTIVE`, `COMPLETED`, `CANCELLED`.
- `service.ParkingService` — Central business logic. Stores slots and reservations in-memory (`ConcurrentHashMap`). Synchronized methods ensure correctness for reserve/occupy/release flows. Contains admin helpers `addSlot` and `removeSlot`.
- `controller.Main` — Small console demo showing search → reserve → occupy → release flow; useful to step through during an oral exam.

Design notes & extensibility
---------------------------
- MVC separation: models are pure POJOs, business logic is in the service layer, and the `Main` class acts as a controller/view for demonstrations. Replacing `Main` with a REST controller or GUI is straightforward.
- Persistence: storage is currently in-memory. To persist data add a DAO/repository layer and replace the in-memory maps in `ParkingService` with repository calls (JPA, JDBC, or any storage).
- Concurrency: service methods are `synchronized` for simplicity. In production you would use finer-grained locking or rely on transactional guarantees from a database.
- Features you can add easily: slot sizes, pricing & billing, multi-level garages, reservation expiration, or authentication for admin/user roles.

Exam talking points
-------------------
- Why MVC: decoupling of model, view/controller, and service simplifies testing and extension.
- Thread-safety choices: `ConcurrentHashMap` for concurrent reads and `synchronized` writes to keep logic simple and clear for an exam.
- How to extend: show a simple Spring Boot controller that calls `ParkingService` and a JPA repository for persistence.

Notes
-----
- The project purposefully uses minimal external libraries (only JUnit for testing) so students can focus on design and behavior.
- The code is intentionally short and well-commented to help explanation in a time-limited exam setting.

If you'd like, I can:
- Add a `README.md` run badges or CI configuration.
- Provide a Spring Boot skeleton to expose REST endpoints for each `ParkingService` operation.
- Make the `Main` controller interactive (command-line prompts).

Enjoy — and let me know which extension you want next.
