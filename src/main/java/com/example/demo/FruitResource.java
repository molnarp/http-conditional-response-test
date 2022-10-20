package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/fruits")
public class FruitResource {

    @RequestMapping("/{name}")
    public ResponseEntity<Fruit> get(@PathVariable("name") String name) {
        try {
            return ResponseEntity.ok()
                    .lastModified(Fruit.LAST_MODIFIED)
                    .eTag(name)
                    .body(Fruit.valueOf(name));
        } catch (IllegalArgumentException | NullPointerException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
