package com.example.sample;

import java.net.URI;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

@Configuration
class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().
      anyRequest().authenticated() // All requests require authentication
      .and()
      .oauth2ResourceServer().jwt(); // validates access tokens as JWTs

    http.cors();
  }
}
