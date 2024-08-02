# TestContainers Example

This project demonstrates how to use TestContainers to set up and run Docker containers for testing purposes.



## Prerequisites

- Docker installed and running on your machine
- Java JDK 8 or higher
- Maven or Gradle (depending on your project setup)

## Running the Example

1. Clone this repository
2. Navigate to the project directory
3. Run the `TestContainersExample` class by running

```
./gradlew run
```

## Inspecting the Containers

After running the example, you can inspect the created containers using Docker commands.

### Check Network Aliases

To check the network aliases for each container:

```bash
docker ps
# Note the container IDs or names

docker inspect <container_id_or_name> | grep Alias -A10
"Aliases": [
    "mockserver",
    "<container_id>"
],
```

### Verify Containers are on the Same Network
To check that both containers are on the same network:

```
docker network ls
# Note the network ID that starts with "testcontainers-"

docker network inspect <network_id>
```

In the output, you should see both containers listed under the "Containers" section.

### Interacting with the Containers

You can interact with the containers using the docker exec command.

### Accessing a Container's Shell
```
docker exec -it <container_id_or_name> bash
```

### Curling Between Containers

Once you're inside a container, you can curl the other container using its network alias:

From the mockserver container:

```
curl http://cart-api
```

From the cart-api container:

```
curl http://mockserver
```


Both should return the default Nginx welcome page, as we're using the nginx:latest image for both containers.


### Cleaning Up

The containers will automatically be removed when the Java program exits. If you need to manually remove them:

```
docker stop <container_id_or_name>
docker rm <container_id_or_name>
```

Replace `<container_id_or_name>` with the actual container ID or name for each container.


