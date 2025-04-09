package com.bubusyaka.demo.repository.jpa;

import com.bubusyaka.demo.model.entity.DeliveryAllowedCityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AllowedCitiesRepository extends JpaRepository<DeliveryAllowedCityEntity, Long> {

    @Query(value = """
select city_name from delivery_allowed_city dac
where dac.id = :id 
limit 1
""", nativeQuery = true)
    String findOrderCityName(@Param("id") Long id);
}
