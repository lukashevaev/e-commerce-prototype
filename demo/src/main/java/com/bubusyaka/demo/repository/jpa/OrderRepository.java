package com.bubusyaka.demo.repository.jpa;

import com.bubusyaka.demo.model.dto.CompositeDTO;
import com.bubusyaka.demo.model.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ORDER")
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {

    @Modifying
    @Query(value = "UPDATE orders set is_completed = true, completion_date = CURRENT_DATE where is_completed = false returning *", nativeQuery = true)
    List<OrderEntity> markAllCompleted();

    @Modifying
    @Query(value = """

            update
	delivery_time dt
set
	estimated_days = avg_days
from (
select
		wk, avg_days
	from
		(
		select
			(p.provider_city || dac2.city_name) as wk,
			avg(date_part('day', o.completion_date - o.creation_date)) as avg_days
		from
			orders o
		join item i
on
			o.item_id = i.id
		join providers p
on
			i.provider_id = p.id
		join delivery_allowed_city dac
on
			p.provider_city = dac.city_name
		join delivery_allowed_city dac2
on
			o.city_id = dac2.id
		where
			o.is_completed = true
		group by
			(p.provider_city || dac2.city_name))
)
where
	wk = dt.way_key;
""", nativeQuery = true)
    void renewEstimatedTime();

@Query(value = """
select provider_city from providers p\s
where p.id = (select provider_id from item i \s
                                 where i.id = :id)
limit 1
""", nativeQuery = true)
String findProviderCityName(@Param("id") Long id);

    @Query(value = """
select * from new_orders where is_completed = true
""", nativeQuery = true)
    List<NewOrderEntity> findCompletedOrders();

    @Query(value = """
SELECT NEW com.bubusyaka.demo.model.dto.CompositeDTO(o.id, o.itemId, i.name, o.userId, i.productCategory, i.ageCategory, ut.age) from OrderEntity o
join ItemEntity i \s
on o.itemId = i.id \s
join UserEntity ut \s
on o.userId = ut.id \s
where ut.id = :id
order by o.completionDate desc
""")
    List<CompositeDTO> findItemsInfoOrderedByUsers(@Param("id") Long id);

    @Query(value = """
SELECT NEW com.bubusyaka.demo.model.dto.CompositeDTO(o.id, o.itemId, i.name, o.userId, i.productCategory, i.ageCategory, ut.age) from OrderEntity o
join ItemEntity i \s
on o.itemId = i.id \s
join UserEntity ut \s
on o.userId = ut.id \s
order by o.completionDate desc
""")
    List<CompositeDTO> findItemsInfoOrderedByUsersNoID();

}