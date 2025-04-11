package com.bubusyaka.demo.repository.jpa;

import com.bubusyaka.demo.model.entity.NewOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewOrderRepository extends JpaRepository<NewOrderEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "call InsertNonCompletedOrdersToNewOrders()", nativeQuery = true)
    void callInsertNonCompletedOrdersProcedure();

    @Modifying
    @Query(value = "UPDATE new_orders set is_completed = true, completion_date = CURRENT_DATE where is_completed = false returning *", nativeQuery = true)
    List<NewOrderEntity> markAllCompleted();

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
			new_orders o
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

    @Modifying
    @Query(value = """
delete FROM new_orders
WHERE id in :id
""", nativeQuery = true)
    void deleteCompletedOrdersFromNewTable(@Param("id") List<Long> id);

    @Modifying
    @Query(value = """
with deleted_orders as (
    delete from new_orders\s
    where id in (
    	select id from new_orders\s
    	order by creation_date\s
        limit (select trunc(random() * 4 + 1))
        )
             \s
    returning item_id, city_id, is_completed, creation_date, current_date as completion_date, user_id
)
insert into orders (item_id, city_id, is_completed, creation_date, completion_date, user_id)
select * from deleted_orders
returning *
""", nativeQuery = true)
    List<NewOrderEntity> transferCompletedOrders();

    @Query(value = """
select count(*) from new_orders
""", nativeQuery = true)
    List<NewOrderEntity> countNewOrders();

}




