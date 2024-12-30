package com.loiev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService {

    private final ScheduledExecutorService scheduledExecutorService;
    private Instant startTime;
    private boolean isTaskOneEnabled = false;

    @Autowired
    public TaskService(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @PostConstruct
    public void init() {
        startTime = Instant.now();
        askTaskOne();
        startTaskTwo();
    }

    private void askTaskOne() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enable task one? (Y/N): ");
        String answer = scanner.nextLine().trim().toUpperCase();
        isTaskOneEnabled = "Y".equals(answer);

        if (!isTaskOneEnabled) {
            System.out.println("Task one skipped.");
        }
    }

    @Scheduled(fixedRate = 10000)
    public void taskOne() {
        if (isTaskOneEnabled) {
            long elapsedSeconds = Duration.between(startTime, Instant.now()).toSeconds();
            System.out.println("Task one executed. Time since start: " + elapsedSeconds + " seconds.");
        }
    }

    private void startTaskTwo() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            long elapsedSeconds = Duration.between(startTime, Instant.now()).toSeconds();
            System.out.println("Task two executed. Time since start: " + elapsedSeconds + " seconds.");
        }, generateRandomInterval(), generateRandomInterval(), TimeUnit.SECONDS);
    }

    private long generateRandomInterval() {
        Random random = new Random();
        return random.nextInt(10) + 1; // Random interval between 1 and 10 seconds
    }
}
