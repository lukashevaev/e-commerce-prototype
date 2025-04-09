package com.bubusyaka.demo.controller;

import com.bubusyaka.demo.model.dto.MostPopularOrdersDto;
import com.bubusyaka.demo.model.entity.OrderEntity;
import com.bubusyaka.demo.repository.dao.OrderDao;
import com.bubusyaka.demo.repository.jpa.OrderRepository;
import com.bubusyaka.demo.utils.TryUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.bubusyaka.demo.utils.TryUtils.tryCatch;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Shows info about orders")
public class OrderController {

    private final OrderDao orderDao;

//    @GetMapping
//    public ResponseEntity<List<OrderEntity>> getAllOrders(@RequestParam(required = false) String itemId,
//                                                          @RequestParam(required = false) String cityId) {
//        // TODO mb change to Spring Exception Handler or Spring Advice
//        return tryCatch(() -> orderDao.findOrdersByProviderAndCity(
//                itemId != null ? Long.valueOf(itemId) : null,
//                cityId != null ? Long.valueOf(cityId) : null));
//    }

    @GetMapping("/bubu")
    @Operation(summary = "shows orders with those parameters")
    public ResponseEntity<List<MostPopularOrdersDto>> getOrders(@Parameter(description = "limit of orders to be searched")
            @RequestParam String limit,
                                                                @Parameter(description = "startDate of orders to be searched")
                                                                @RequestParam(required = false) String startDate,

                                                                @Parameter(description = "endDate of orders to be searched")
                                                                @RequestParam(required = false) String endDate,

                                                                @Parameter(description = "lastNDays of orders to be searched")
                                                                @RequestParam(required = false) String lastNDays,

                                                                @Parameter(description = "cityId of orders to be searched")
                                                                @RequestParam(required = false) String cityId,

                                                                @Parameter(description = "providerName of orders to be searched")
                                                                @RequestParam(required = false) String providerName) {
        if (startDate == null && endDate == null && lastNDays == null) {
            var problem = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), "Not given any of required parameters");
            return ResponseEntity.of(problem).build();
        }

        return tryCatch(() -> orderDao.findMostPopularOrders(
                startDate != null ? LocalDate.parse(startDate) : null,
                endDate != null ? LocalDate.parse(endDate) : null,
                lastNDays != null ? Integer.valueOf(lastNDays) : null,
                limit != null ? Integer.valueOf(limit) : null,
                cityId != null ? Long.valueOf(cityId) : null,
                providerName)
        );
    }
}
