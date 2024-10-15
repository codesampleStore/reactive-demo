package com.example.reactivedemo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;


public interface AstroInterface {

  @GetExchange("/astros.json")
  Mono<AstroService.AstroResponse> getAstroResponse();

  @GetExchange("/iss-now.json")
  Mono<String> getIssPosition();
}
