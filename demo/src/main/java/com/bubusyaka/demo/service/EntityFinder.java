package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.entity.Auditable;
import com.bubusyaka.demo.model.enums.AuditableEntities;
import com.bubusyaka.demo.repository.jpa.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntityFinder {
    //Можно по map достать beans, где String - тип названия бина
    // only auditable repositories
    @Autowired
    private Map<String, JpaRepository<? extends Auditable, Long>> repositories;

    @Autowired
    private Map<String, EntityFormatter> entityFormatters;

    public <T extends Auditable> T findById(AuditableEntities entityName, Long id) {
        var repository = (JpaRepository<T, Long>) repositories.get(entityName.name());

        return repository.findById(id).get();
    }

    public <T> String formatEntity(AuditableEntities entityName, T entity){
        var name = entityName.name().concat("_FORMATTER");
        var entityFormatter = entityFormatters.get(name);

        return entityFormatter.convert(entity);
    }
}
