package com.fintech.database.service.impl;

import com.fintech.database.dto.kudago.EventListDto;
import com.fintech.database.dto.kudago.EventsDto;
import com.fintech.database.service.HttpService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecuteEventsService implements HttpService<EventsDto> {

    private final WebClient webClient;

    @Value("${url.events}")
    private String urlPath;

    @SneakyThrows
    @Override
    public List<EventsDto> getListByApi() {
        List<EventsDto> resultList = new ArrayList<>();

        var ref = new Object() {
            int pageNumber = 1;
            final long actualSince = System.currentTimeMillis() / 1000L;
            final long actualUntil = Instant.now().plus(7, ChronoUnit.DAYS).getEpochSecond();
        };

        var isLastPage = true;

        while (isLastPage) {
            EventListDto list = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(urlPath)
                            .queryParam("expand", "location")
                            .queryParam("page", ref.pageNumber)
                            .queryParam("page_size", 100)
                            .queryParam("fields", "id,title,dates,price,location")
                            .queryParam("actual_since", ref.actualSince)
                            .queryParam("actual_until", ref.actualUntil)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(EventListDto.class)
                    .toFuture()
                    .get();

            resultList.addAll(list.getResults());
            if (list.getNext() == null) {
                isLastPage = false;
            }
            ref.pageNumber++;
        }
        return resultList;
    }
}