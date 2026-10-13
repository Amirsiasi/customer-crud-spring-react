package com.example.customer;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    void save_and_find_by_id() {

        Customer customer = new Customer("John", "Doe", "johnDoe@gmail.com");
        Customer saved = repository.save(customer);

        Optional<Customer> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
        assertThat(found.get().getLastName()).isEqualTo("Doe");
        assertThat(found.get().getEmail()).isEqualTo("johnDoe@gmail.com");



    }


    @Test
    void update_customer_by_firstName() {

        Customer c = repository.save(new Customer("Jane", "Doe", "johnDoe@gmail.com"));

        c.setFirstName("Mark");

        Customer updated = repository.save(c);

        assertThat(updated.getFirstName()).isEqualTo("Mark");
    }

    @Test
    void delete_customer_by_id(){
        Customer c = repository.save(new Customer("Jane", "Doe", " johnDoe@gmail.com "));
        Long id = c.getId();
        repository.deleteById(id);
        assertThat(repository.findById(id)).isEmpty();
    }




}