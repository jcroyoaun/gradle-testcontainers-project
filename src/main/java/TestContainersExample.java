import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestContainersExample {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
        setSystemProperties();
    }

    public static void main(String[] args) {
        try {
            runContainers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadProperties() {
        try (InputStream input = TestContainersExample.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void setSystemProperties() {
        System.setProperty("DOCKER_HOST", properties.getProperty("docker.host"));
        System.setProperty("TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE", properties.getProperty("docker.socket.override"));
        System.setProperty("testcontainers.ryuk.disabled", properties.getProperty("testcontainers.ryuk.disabled"));
        System.setProperty("testcontainers.dockerclient.strategy", properties.getProperty("testcontainers.dockerclient.strategy"));
    }

    private static void runContainers() throws InterruptedException {
        Network network = Network.newNetwork();

        GenericContainer<?> mockserver = createContainer("mockserver", properties.getProperty("mockserver.image"), network);
        GenericContainer<?> cartApi = createContainer("cart-api", properties.getProperty("cartapi.image"), network);

        startContainer(mockserver, "mockserver");
        startContainer(cartApi, "cart-api");

        Thread.sleep(Long.parseLong(properties.getProperty("container.wait.time")));
    }

    private static GenericContainer<?> createContainer(String alias, String imageName, Network network) {
        return new GenericContainer<>(DockerImageName.parse(imageName))
                .withNetwork(network)
                .withNetworkAliases(alias)
                .withExposedPorts(80);
    }

    private static void startContainer(GenericContainer<?> container, String name) {
        System.out.println("Starting " + name + " container...");
        container.start();
    }
}
