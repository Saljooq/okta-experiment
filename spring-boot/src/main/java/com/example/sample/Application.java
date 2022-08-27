package com.example.sample;

import java.net.URI;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// REPLACE THE FOLLOWING DEPRECATED CLASS IF POSSIBLE
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter; // This is deprecated

// THIS IS THE UPGRADED VERSION TO USE
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RestController
    static class SimpleRestController {

        @Autowired
        OAuth2AuthorizedClientService oAuthorizedClientService;


        @GetMapping("/")
        String sayHello(@AuthenticationPrincipal OidcUser oidcUser, OAuth2AuthenticationToken o) {
            String msg = "<p>Name : " + oidcUser.getFullName()+"</p><br>";
            msg += "<p>Access Token Hash :" + oidcUser.getAccessTokenHash() + "</p><br><p>";
            msg += "Email : " + oidcUser.getEmail() + " </p><br><p>";
            msg += "Authorized party : " + oidcUser.getAuthorizedParty() + " </p><br><p>";

            msg += " </p><br>ACCESS TOKEN : <p>";

            OAuth2AuthorizedClient client =
              oAuthorizedClientService.loadAuthorizedClient(o.getAuthorizedClientRegistrationId(), o.getName());

            msg += client.getAccessToken().getTokenValue();


            return msg;

        }

        @GetMapping("jwt")
        public String jwt(OAuth2AuthenticationToken authentication) {

          OAuth2AuthorizedClient client =
              oAuthorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
      
          return client.getAccessToken().getTokenValue();
        }

        @GetMapping("/hello")
        String hello(){
            return "Hello there!";
        }

        @GetMapping("/resources")
        String resources(Principal p){
            return "<p> hello there ! " + p.getName() + "</p>";
        }

        @GetMapping("/redirect")
        ResponseEntity<Void> redirect() {
          return ResponseEntity.status(HttpStatus.FOUND)
              .location(URI.create("http://www.yahoo.com"))
              .build();
        }
    }

}

// IF THE APP WORKS WITHOUT THE METHOD BELOW REMOVE IT - IT HAS BEEN OFFICIALLY
// DEPRECATED BY SPRING

// @Configuration
// class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

//   @Override
//   protected void configure(HttpSecurity http) throws Exception {
//     http.authorizeRequests().
//       anyRequest().authenticated() // All requests require authentication
//       .and()
//       .oauth2ResourceServer().jwt(); // validates access tokens as JWTs

//     http.cors();
//   }
// }


// BELOW CODE IS VERY IMPORTANT FOR TO ALLOW FOR CORS - WE MAY DISABLE CORS LATER ON IN PRODUCTION IF NEEDED

@Configuration
class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests((requests) -> requests.anyRequest().authenticated());

        // enables OAuth redirect login
        http.oauth2Login();
    
        // enables OAuth Client configuration
        http.oauth2Client();
    
        // enables REST API support for JWT bearer tokens
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        http.cors();
    
        return http.build();
    }

}


@Configuration
@EnableWebMvc
class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}