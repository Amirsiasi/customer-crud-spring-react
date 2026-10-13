package com.example.customer;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper json;

    @MockBean
    private CustomerRepository repository;

    @Test
    void get_all_customers() throws Exception {
        Customer c = new Customer("John", "Doe", "johndeo@gmail.com");
        c.setId(1L);

        when(repository.findAll()).thenReturn(List.of(c));

        mvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("johndeo@gmail.com")));
    }

    @Test
    void get_one_foundand_not_found() throws Exception {
        Customer c = new Customer("John", "Doe", "Johandoe@gmail.com");
        c.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(c));
        when(repository.findById(2L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Johandoe@gmail.com")));

        mvc.perform(get("/api/customers/2"))
                .andExpect(status().isNotFound());


    }

    @Test
    void create_customer() throws Exception {


        when(repository.save(any(Customer.class))).thenAnswer(inv -> {
            Customer in = inv.getArgument(0);
            in.setId(1L);
            return in;
        });


        Customer body = new Customer("John", "Doe", "Johandoe@gmail.com");

        mvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/customers/1"))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("Johandoe@gmail.com")));
    }

    @Test
    void delete_customer() throws Exception {
        when(repository.existsById(1L)).thenReturn(true);


        mvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
        verifyNoMoreInteractions(repository);

    }

    @Test
    void delete_customer_not_found() throws Exception {
        when(repository.existsById(1L)).thenReturn(false);

        mvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNotFound());

        verify(repository).existsById(1L);
        verify(repository, never()).deleteById(anyLong());


    }

}