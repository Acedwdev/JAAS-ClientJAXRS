
package test;

import domain.User;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;


public class TestRestWS {
    public static void main(String[] args) {
        
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().nonPreemptive().credentials("admin", "admin").build();
        
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(feature);
        
        Client client = ClientBuilder.newClient(clientConfig);
        
        WebTarget webTarget = client.target("http://localhost:8080/sms-jakartaee-web/webservice").path("/users");
        
        User user = webTarget.path("/2").request(MediaType.APPLICATION_XML).get(User.class);
        System.out.println("Found user:" + user);
    }
}
