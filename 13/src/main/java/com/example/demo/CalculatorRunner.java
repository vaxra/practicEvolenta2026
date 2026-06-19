package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@ConditionalOnProperty(name = "calculator.runner.enabled", havingValue = "true", matchIfMissing = true)
public class CalculatorRunner implements CommandLineRunner {

    private final Calculator calculator;

    public CalculatorRunner(Calculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите число a");
        double a = scanner.nextDouble();
        System.out.println("Введите число b");
        double b = scanner.nextDouble();
        System.out.println("Введите тип операции: " + calculator.getSupportedOperations());
        String operationType = scanner.next();
        calculator.calc(operationType, a, b);
    }
}
