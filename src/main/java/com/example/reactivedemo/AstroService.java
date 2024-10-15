package com.example.reactivedemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;

@Service
public class AstroService {
  public record Assignment(String name, String craft) {
  }

  public record AstroResponse(String message, int number, List<Assignment> people) {
  }
  private final RestTemplate template;
  private final RestClient restClient;
  private final WebClient webClient;

  public AstroService(RestTemplateBuilder builder,
                      @Value("${astro.baseUrl}") String baseUrl) {
    this.restClient = RestClient.create(baseUrl);
    this.template = builder.rootUri(baseUrl).build();
    this.webClient = WebClient.create(baseUrl);
  }

  public String getPeopleInSpace() {
    return template.getForObject("/astros.json", String.class);
  }

  public AstroResponse getAstroResponseSync() {
    return template.getForObject(
        "/astros.json",
        AstroResponse.class);
  }

  // It is synchronous
  public AstroResponse getAstroResponseRestClient() {
    return restClient.get()
        .uri("/astros.json")
        .retrieve()
        .body(AstroResponse.class);
  }

  public CompletableFuture<ResponseEntity<String>> getAstroResponseWebClient() {
    long startTimeInMs = System.currentTimeMillis();
    return webClient.get()
        .uri("/astros.json")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntity(String.class)
        .doOnSuccess(d -> {
          System.out.println(format("returned success response, responseSize: %s, took: %s",
              d.getBody() != null ? d.getBody().length() : 0, System.currentTimeMillis() - startTimeInMs));
          // Add Metrics (success, failure, completionTime)
        })
        .doOnError(e -> {
          System.out.println(e);

          // Add Metrics  (success, failure, completionTime)
        })
        .toFuture();
  }

  public Mono<ResponseEntity<String>> getAstroResponseWebClient2() {
    System.out.println("getAstroResponseWebClient2 START");
    long startTimeInMs = System.currentTimeMillis();
    Mono<ResponseEntity<String>> responseEntityMono = null;
    try {
      responseEntityMono = webClient.get()
          .uri("/astros.json")
          .accept(MediaType.APPLICATION_JSON)
          .retrieve()
          .toEntity(String.class)
          .doOnSuccess(d -> {
      //      System.out.println(format("getAstroResponseWebClient2 - returned success response, responseSize: %s, took: %s",
      //          d.getBody() != null ? d.getBody().length() : 0, System.currentTimeMillis() - startTimeInMs));
            // Add Metrics (success, failure, completionTime)
          })
          .doOnError(e -> {
            System.out.println("getAstroResponseWebClient2 ERROR:" + e);
            e.printStackTrace();
          });
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("getAstroResponseWebClient2 END");
    return responseEntityMono;
  }
}
