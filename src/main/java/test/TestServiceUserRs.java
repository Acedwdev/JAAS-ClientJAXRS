package test;

import domain.User;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class TestServiceUserRs {

    private static final String URL_BASE = "http://localhost:8080/sms-jakartaee-web/webservice";
    private static Client client;
    private static WebTarget webTarget;
    private static User user;
    private static List<User> users;
    private static Invocation.Builder invocationBuilder;
    private static Response response;

    public static void main(String[] args) {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().nonPreemptive().credentials("admin", "admin").build();

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(feature);

        client = ClientBuilder.newClient(clientConfig);        

        webTarget = client.target(URL_BASE).path("/users");

        user = webTarget.path("/1").request(MediaType.APPLICATION_XML).get(User.class);
        System.out.println("Found User:" + user);

        users = webTarget.request(MediaType.APPLICATION_XML).get(Response.class).readEntity(new GenericType<List<User>>() {
        });
        System.out.println("\nFound Users");
        printUsers(users);

        User newUser = new User();
        newUser.setName("Hugh");
        newUser.setLastName("Mollins");
        newUser.setEmail("hmollins3@mail.com");
        newUser.setPhone("1425-9685");

        invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
        response = invocationBuilder.post(Entity.entity(newUser, MediaType.APPLICATION_XML));
        System.out.println("");
        System.out.println(response.getStatus());

        User foundUser = response.readEntity(User.class);
        System.out.println("Persona agregada:" + foundUser);

        User modifyUser = foundUser;
        modifyUser.setLastName("Chandler");
        String pathId = "/" + modifyUser.getIdUser();
        invocationBuilder = webTarget.path(pathId).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.put(Entity.entity(modifyUser, MediaType.APPLICATION_XML));

        System.out.println("");
        System.out.println("response:" + response.getStatus());
        System.out.println("Modify User:" + response.readEntity(User.class));

        User deleteUser = foundUser;
        String pathEliminarId = "/" + deleteUser.getIdUser();
        invocationBuilder = webTarget.path(pathEliminarId).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.delete();
        System.out.println("");
        System.out.println("response:" + response.getStatus());
        System.out.println("Delete User" + deleteUser);

    }

    private static void printUsers(List<User> users) {
        for (User u : users) {
            System.out.println("User:" + u);
        }
    }
}
