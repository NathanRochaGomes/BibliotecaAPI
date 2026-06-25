package com.example.BibliotecaAPI.controller;

import com.example.BibliotecaAPI.dto.*;
import com.example.BibliotecaAPI.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {
    @Autowired private StatsService statsService;

    @GetMapping("/summary")
    public ResponseEntity<StatsSummaryDTO> getSummary() {
        return ResponseEntity.ok(statsService.getSummary());
    }

    @GetMapping("/top-books")
    public ResponseEntity<List<TopBookDTO>> getTopBooks() {
        return ResponseEntity.ok(statsService.getTopBooks());
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<OverdueLoanDTO>> getOverdueLoans() {
        return ResponseEntity.ok(statsService.getOverdueLoans());
    }
}