package com.airline.reservation.service;

import com.airline.reservation.dto.inngest.InngestEventRequestDto;
import com.airline.reservation.dto.inngest.InngestEventResponseDto;
/**
 * Service contract for publishing airline events to Inngest.
 * Hides the Inngest SDK from the controller layer.
 */
public interface InngestService {

    /**
     * Publishes an event to Inngest.
     *
     * @param request event details to publish
     * @return publishing result
     */
    InngestEventResponseDto publishEvent(InngestEventRequestDto request);
}
