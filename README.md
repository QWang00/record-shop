# Record Shop Inventory Management System

## Overview

The **Record Shop Inventory Management System** is a Spring Boot application tailored for efficient album management within a record shop. It provides a RESTful API that facilitates the CRUD operations (Create, Read, Update, Delete) on albums, which are stored in a PostgreSQL database. Users can manage this database using pgAdmin. The application is built with a modular design and follows a layered architecture, which helps in organizing the system's components for easier maintenance and scalability.

## Key Features

- **CRUD Operations on Albums**: Offers endpoints to handle the creation, retrieval, updating, and deletion of album records.
- **Advanced Search**: Enables users to find albums based on criteria such as artist, release year, genre, or name.
- **Global Exception Handling**: Implements a global exception handler to provide uniform and user-friendly error messages.
- **Swagger API Documentation**: Utilizes Springdoc OpenAPI to deliver an interactive interface for exploring and understanding the API endpoints.
- **Health Check Endpoint**: Leverages Spring Boot Actuator to expose a `/health` endpoint for monitoring the system’s health status.

## Assumptions

- The database is set up using **PostgreSQL** and managed via **pgAdmin**.
- The `Album` entity has fields for `id`, `artist`, `releaseYear`, `genre`, and `name`.
- Each album's `id` is automatically generated and incremented by PostgreSQL.
- Genre is validated against a predefined set of values using an `enum` type in the `Album` class.
- The application includes a seed data setup for initial testing, provided by the DemoSeedDataLoader class.
- The application is deployed locally and accessible via `http://localhost:8080/albums/swagger-ui/index.html` for API documentation.

## Approaches

- **Spring Boot Architecture**: The application uses a typical Spring Boot layered architecture with Controllers, Services, and Repositories.
- **OpenAPI and Swagger Integration**: Provides an interactive UI to explore the API and understand available operations. Each endpoint is documented using `@Operation` and `@ApiResponse` annotations.
- **Global Exception Handling**: Custom exceptions like `ItemNotFoundException` are used to manage error scenarios, ensuring consistent and user-friendly error messages.
- **Command-Line Interface (CLI) (Future Addition)**: To provide a better user experience for staff and customers, a command-line interface will be added to allow direct interaction with the inventory system without using a REST client.

## Endpoints

| HTTP Method | Endpoint          | Description                                  |
|-------------|-------------------|----------------------------------------------|
| GET         | `/albums/all`     | Retrieve all albums                         |
| GET         | `/albums/{id}`    | Retrieve an album by its ID                  |
| POST        | `/albums`         | Add a new album to the inventory             |
| PUT         | `/albums/{id}`    | Update an album by its ID                    |
| DELETE      | `/albums/{id}`    | Delete an album by its ID                    |
| GET         | `/albums/by-artist` | Retrieve albums by artist name               |
| GET         | `/albums/by-release-year` | Retrieve albums by release year         |
| GET         | `/albums/by-genre` | Retrieve albums by genre                     |
| GET         | `/albums/by-name`  | Retrieve albums by name                      |

## How to Run the Application

1. **Set up the PostgreSQL Database**:
   - Install PostgreSQL and pgAdmin.
   - Create a new database named `record_shop` and configure it in `application.properties`.

2. **Build the Project**:
   - Clone the repository.
   - Run `mvn clean install` to build the project.

3. **Run the Application**:
   - Start the application using the `RecordShopApplication` class.
   - Access the API documentation at `http://localhost:8080/albums/swagger-ui/index.html`.

4. **Access pgAdmin**:
   - Open pgAdmin and connect to the `record_shop` database to manage data directly.

## Future Enhancements

### Command-Line Interface (CLI)

To enhance user experience, especially for staff who manage the inventory daily, a command-line interface (CLI) will be developed. The CLI will offer the following functionalities:

- **Search Command**: Allow searching for albums by various criteria (e.g., artist, release year, genre, name).
- **Add Album Command**: Enable users to add new albums to the inventory directly from the CLI.
- **Update Album Command**: Allow updating existing albums using simple prompts.
- **Delete Album Command**: Provide the ability to remove albums by ID.

### Implementation Plan

1. **Create a `MainCLI` Class**: This will serve as the entry point for the CLI.
2. **Define a `Command` Interface or Abstract Class**: This will outline the common behavior for all commands.
3. **Implement Specific Commands**:
   - `SearchCommand`
   - `AddAlbumCommand`
   - `UpdateAlbumCommand`
   - `DeleteAlbumCommand`
4. **Utility Class (`CLIUtils`)**: For common tasks like input validation and formatting.
5. **Documentation**: Provide instructions in the README for how to run the CLI and its commands.

### Future Model Enhancements

To further extend the functionality of the record shop system, models for `Price` and `Inventory` will be considered:

- **`Price` Model**: To track the price of albums. It may include fields such as `albumId`, `price`, and `currency`.
- **`Inventory` Model**: To manage stock levels of albums. It may include fields such as `albumId`, `stockQuantity`, and `location`.

These models will help in managing and tracking album prices and inventory levels more effectively.

### Database Migration to AWS RDS

In the future, the project will consider migrating the database to AWS RDS. This will involve creating a new `application-aws.properties` file to configure the connection settings for AWS RDS. This change will enhance scalability and reliability, providing a more robust backend infrastructure.

## Installation and Configuration

- Make sure you have Java, Maven, PostgreSQL, and pgAdmin installed.
- Update the `application.properties` with your PostgreSQL credentials.
- Run the application using the `mvn spring-boot:run` command.

## Conclusion

This project serves as a robust foundation for managing a record shop's inventory system with a RESTful API. The future CLI addition will further enhance usability, providing flexibility for both staff and customers to interact with the inventory system easily.

