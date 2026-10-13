package com.example.customer;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost"})
public class CustomerController {

    private final CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Customer> all() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> one(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public String home() {
        return "Backend is running";
    }


    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer c) {
        Customer saved = repository.save(c);
        return ResponseEntity.created(URI.create("/api/customers/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @Valid @RequestBody Customer c) {
        return repository.findById(id).map(existing -> {
            existing.setFirstName(c.getFirstName());
            existing.setLastName(c.getLastName());
            existing.setEmail(c.getEmail());
            Customer saved = repository.save(existing);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
