package com.bubusyaka.demo.controller;

import com.bubusyaka.demo.model.enums.AuditableEntities;
import com.bubusyaka.demo.service.EntityFinder;
import com.bubusyaka.demo.service.EntityFormatter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Entity finder Controller", description = "Finds an entity type")
public class FindEntityController {

    private final EntityFinder entityFinder;

    /**
     * Используем параметр String, а не enum,
     * чтобы запрос не возвращал ошибку Bad Request,
     * если придет невалидное значение Enum.
     * Т.е. если кто-то отправит запрос для не-auditable сущности,
     * мы должны сами обработать эту ошибку.
     * Если придет Order -
     * он не-auditable и метод не сможет создать enum из неподходящей сущности.
     * короче чтобы вернуть ошибку самому, что ордер не аУдитБл,
     * а не то, что программа тупая и не поняла кто такой ордер
     * из-за несовпадения вводимого с проверяемыс
     */
//    @GetMapping("/find")
//    public LocalDateTime findEntity(@RequestParam String entityName ,
//                                    @RequestParam Long id) {
//        var entityType = AuditableEntities.find(entityName);
//        if (entityType.isEmpty()) {
//            throw new ErrorResponseException(HttpStatus.NOT_FOUND);
//        }
//        var entity = entityFinder.findById(entityType.get(), id);
//
//        return entity.getCreatedDate();
//    }

    @Operation(summary = "Finds entity type by id")
    @GetMapping("/find")
    public String findEntity(@Parameter(description = "entity type")
                                 @RequestParam String entityName ,
                             @Parameter(description = "entity id")
                                @RequestParam Long id) {
        var entityType = AuditableEntities.find(entityName);
        if (entityType.isEmpty()) {
            throw new ErrorResponseException(HttpStatus.NOT_FOUND);
        }
        var entity = entityFinder.findById(entityType.get(), id);

        return entityFinder.formatEntity(entityType.get(), entity);

    }
}
