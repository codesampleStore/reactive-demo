package com.example.reactivedemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AstroServiceTest {

  @Autowired
  private AstroService service;

  @Test
  void getPeopleInSPace() {
    String people = service.getPeopleInSpace();
    assertNotNull(people);
    assertTrue(people.contains("people"));
    System.out.println(people);
  }

  @Test
  void getAstroResponseSync() {
    AstroService.AstroResponse response = service.getAstroResponseSync();
    assertNotNull(response);
    assertEquals("success", response.message());
    assertTrue(response.number() >= 0);
    assertEquals(response.number(), response.people().size());
    System.out.println(response);
  }

  /**
   * RestClient and RestTemplate are synchronous
   */
  @Test
  void getAstroResponseSyncRestClient() {
    AstroService.AstroResponse response = service.getAstroResponseRestClient();
    assertNotNull(response);
    assertEquals("success", response.message());
    assertTrue(response.number() >= 0);
    assertEquals(response.number(), response.people().size());
    System.out.println(response);
  }
/*
  @Test
  void getAstroResponseAsyncWebClient() {
    AstroService.AstroResponse response = service.getAstroResponseWebClient().block();
    assertNotNull(response);
    assertEquals("success", response.message());
    assertTrue(response.number() >= 0);
    assertEquals(response.number(), response.people().size());
    System.out.println(response);
  }

  @Test
  void getAstroResponseAsyncWebClientStepVerifier() {

    StepVerifier.create(service.getAstroResponseWebClient()).assertNext(
            response -> {
              assertNotNull(response);
              assertEquals("success", response.message());
              assertTrue(response.number() >= 0);
              assertEquals(response.number(), response.people().size());
              System.out.println(response);
            })
        .verifyComplete();
  }
  */

  @Test
  void getAstroResponseAsyncWebClientStepVerifier() throws ExecutionException, InterruptedException {

    service.getAstroResponseWebClient()
        .whenComplete((response, error) -> {
          System.out.println(response.getBody() + " - " + error);
        })
        .get();
  }

  @Test
  void getAstroResponseAsyncWebClientStepVerifier2() throws ExecutionException, InterruptedException {

    CompletableFuture<ResponseEntity<String>> future = service.getAstroResponseWebClient2().toFuture();

  //  for (int i=0; i<10; i++) {
  //    System.out.println(i+" ---- ");
      future
          .whenComplete(((responseEntity, throwable) -> {
            System.out.println(responseEntity);
          }));
    //  Thread.sleep(1000);
   // }
  }
}