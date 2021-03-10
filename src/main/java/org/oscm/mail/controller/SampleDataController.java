package org.oscm.mail.controller;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@RestController
public class SampleDataController {

  @GetMapping("/data/emails")
  public ResponseEntity getEmails() throws IOException {

    ResourceLoader resourceLoader = new DefaultResourceLoader();
    Resource resource = resourceLoader.getResource("classpath:static/email_list.json");

    Path path = Paths.get(resource.getURI());
    Stream<String> lines = Files.lines(path);

    StringBuilder jsonData = new StringBuilder();
    lines.forEach(jsonData::append);

    return ResponseEntity.ok(jsonData.toString());
  }
}
