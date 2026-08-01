package com.airline.reservation.controller;

import com.airline.reservation.dto.common.InngestEventRequestDto;
import com.airline.reservation.dto.common.InngestEventResponseDto;
import com.airline.reservation.service.InngestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * REST controller for publishing airline events to Inngest.
 * Delegates all processing to the service layer.
 */
@RestController
@RequestMapping("/api/inngest")
@RequiredArgsConstructor
public class InngestController {

    private final InngestService inngestService;
    /**
     * Publishes an event to Inngest for asynchronous processing.
     */
    @PostMapping("/events")
    public ResponseEntity<InngestEventResponseDto> publishEvent(
            @Valid @RequestBody InngestEventRequestDto request
    ) {
        InngestEventResponseDto response = inngestService.publishEvent(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
