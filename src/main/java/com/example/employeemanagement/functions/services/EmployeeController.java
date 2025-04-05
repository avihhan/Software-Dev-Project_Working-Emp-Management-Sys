package com.example.employeemanagement.functions.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeLoginService loginService;
    private final EmployeeDataService dataService;
    private final EmployeeSearchService searchService;
    private final EmployeeUpdateService updateService;
    private final EmployeeSalaryService salaryService;

    @Autowired
    public EmployeeController(EmployeeLoginService loginService, 
                            EmployeeDataService dataService,
                            EmployeeSearchService searchService,
                            EmployeeUpdateService updateService,
                            EmployeeSalaryService salaryService) {
        this.loginService = loginService;
        this.dataService = dataService;
        this.searchService = searchService;
        this.updateService = updateService;
        this.salaryService = salaryService;
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

    @GetMapping("/search")
    public ResponseEntity<?> searchEmployees(
            @RequestParam String searchBy,
            @RequestParam String searchValue) {
        
        return ResponseEntity.ok(searchService.searchEmployees(searchBy, searchValue));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEmployee(
            @RequestParam String empid,
            @RequestParam String columnToUpdate,
            @RequestParam String newValue) {

        return ResponseEntity.ok(updateService.updateEmployee(empid, columnToUpdate, newValue));
    }

    @PutMapping("/update-salary")
    public ResponseEntity<?> updateSalary(
            @RequestParam int empid,
            @RequestParam double percentageIncrease) {
        
        return ResponseEntity.ok(salaryService.updateSalary(empid, percentageIncrease));
    }
}
