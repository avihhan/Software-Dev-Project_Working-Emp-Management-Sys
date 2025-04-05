package com.example.employeelogin.controllers;

import com.example.employeelogin.services.EmployeeLoginService;
import com.example.employeelogin.services.EmployeeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeLoginService loginService;
    private final EmployeeDataService dataService;

    @Autowired
    public EmployeeController(EmployeeLoginService loginService, EmployeeDataService dataService) {
        this.loginService = loginService;
        this.dataService = dataService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String empid,
            @RequestParam String password) {
        
        boolean isAuthenticated = loginService.authenticate(empid, password);
        
        if (isAuthenticated) {
            return ResponseEntity.ok().body(dataService.getEmployeeDetails(empid));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials. Access denied.");
        }
    }

    @GetMapping("/details/{empid}")
    public ResponseEntity<?> getEmployeeDetails(@PathVariable String empid) {
        return ResponseEntity.ok().body(dataService.getEmployeeDetails(empid));
    }
}
