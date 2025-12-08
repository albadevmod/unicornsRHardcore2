# TextAdventure Game

This project is a Java-based text adventure game, packaged for easy setup and sharing using Docker and Docker Compose.

## Prerequisites
- [Docker](https://www.docker.com/get-started) installed
- [Docker Compose](https://docs.docker.com/compose/install/) (usually included with Docker Desktop)
- (Optional) [Java 17](https://adoptium.net/) and [Maven](https://maven.apache.org/) if you want to build the JAR yourself

## Quick Start (Recommended)
1. **Clone or download the project folder.**
2. **Open a terminal in the project root.**
3. **Build and run the app using Docker Compose:**
   ```
   docker compose up --build
   ```
   - This will build the Docker image and start the application.
   - The app will be available on [http://localhost:8080](http://localhost:8080) (if it exposes a web interface).

## Manual Docker Build & Run
1. **Build the Docker image:**
   ```
   docker build -t textadventure:latest -f dockerfile .
   ```
2. **Run the container:**
   ```
   docker run --rm -p 8080:8080 textadventure:latest
   ```

## Building the JAR (if needed)
If the `target` folder or JAR file is missing, build it with Maven:
1. **Install Java 17 and Maven.**
2. **Run:**
   ```
   mvn clean package
   ```
   - The JAR will be created in the `target` folder.