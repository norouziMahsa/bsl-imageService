# Image Service
An application that can serve optimized images based on predefined properties.

## Requirements
1. Java - 1.11.x
2. Maven - 3.x.x

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/norouziMahsa/ImageService.git
```
**2. Build and run the app using maven**

you can run the app using

```bash
mvn spring-boot:run
```
The app will start running at <http://localhost:7575>.

## Rest API Documentation

You can find the APIs documentation for this application on

<http://localhost:7575/swagger-ui.html/>

## Components

* **ImageController**

  provides REST apis for showing and flushing images


* **ImageService**

  Provides required logics, including storing, optimizing and retrieving images


* **SourceService**

  Provides required functionality for downloading original image from source root

* **S3Repository**

  Provides access to the data stored in S3 storage

## Cache

  Currently, the app uses an in memory cache for a better performance.

## Logging

 For local development the logs are written to the console, for production environment the logs are written
 to the database as configured in application.yml .

## ToDo

* **Integration and unit Tests**

  I liked to add unit and integration tests for REST services.


* **Security**

  The APIs need Authentication.

