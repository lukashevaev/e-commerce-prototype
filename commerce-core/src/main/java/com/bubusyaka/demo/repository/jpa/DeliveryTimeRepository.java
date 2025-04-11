package com.bubusyaka.demo.repository.jpa;

import com.bubusyaka.demo.model.entity.DeliveryTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryTimeRepository extends JpaRepository<DeliveryTimeEntity, Long> {

    @Query(value = """
select o.id, p.provider_city||dac.city_name as "waykey" ,\s
date_part('day',o.completion_date - o.creation_date)  as "delivery_time",
dt.estimated_days\s
from new_orders o join item i
on o.item_id = i.id\s
join providers p\s
on i.provider_id = p.id\s
join delivery_allowed_city dac\s
on o.city_id = dac.id
join delivery_time dt\s
on p.provider_city||dac.city_name = dt.way_key\s
""", nativeQuery = true)
    List<DeliveryTimeEntity> estimatedTime();
}
