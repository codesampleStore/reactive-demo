package com.example.reactivedemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AstroInterfaceTest {

  @Autowired
  private AstroInterface astroInterface;

  @Test
  void getAstroResponse() {
    astroInterface.getAstroResponse()
        .doOnNext( response -> {
          assertNotNull(response);
          assertEquals("success", response.message());
          assertTrue(response.number() >= 0);
          assertEquals(response.number(), response.people().size());
          System.out.println(response);
        }).block();
  }

  @Test
  void getIssPosition() throws ExecutionException, InterruptedException {
    CompletableFuture<String> stringCompletableFuture = astroInterface.getIssPosition().log()
        .toFuture();
    stringCompletableFuture.whenComplete(this::handleResponse).get();

  }

  private void handleResponse(String responseEntity, Throwable throwable) {
    System.out.println(responseEntity);
  }

  @Test
  void getAstroResponseFromInterface(@Autowired AstroInterface astroInterface) {
    AstroService.AstroResponse response = astroInterface.getAstroResponse()
        .block(Duration.ofSeconds(2));
    assertNotNull(response);
    assertAll(
        () -> assertEquals("success", response.message()),
        () -> assertTrue(response.number() >= 0),
        () -> assertEquals(response.number(), response.people().size())
    );
    System.out.println(response);
  }

  @Test
  void getAstroResponseFromInterface2(@Autowired AstroInterface astroInterface) {
    astroInterface.getAstroResponse().toFuture()
        .whenComplete((astro, e) -> {
          System.out.println("ASTRO="+astro);
        });
  }
}