package com.example.demo;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Product;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.ProductRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DataController {

    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;

    public DataController(EmployeeRepository employeeRepository, ProductRepository productRepository) {
        this.employeeRepository = employeeRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
