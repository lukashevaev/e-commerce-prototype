package com.bubusyaka.demo.repository.jpa;

import com.bubusyaka.demo.model.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MostDelayedProvidersRepository extends JpaRepository<ItemEntity, Long> {

    @Query(value = """ 
            select
	provider_name,
	count(delay),
	avg(delay) as average_delay
from\s
	(
	select
		provider_name,
		provider_city,
		city_name,
		delay
	from
		(
		select
			p.provider_name,
			p.provider_city,
			dac2.city_name,
			(date_part('day',
			completion_date - creation_date) - estimated_days) as delay
		from
			public.orders o
		join public.item i
	on
			o.item_id = i.id
		join public.providers p\s
	on
			i.provider_id = p.id
		join public.delivery_allowed_city dac\s
	on
			p.provider_city = dac.city_name
		join public.delivery_allowed_city dac2\s
on
			o.city_id = dac2.id
		join public.delivery_time dt\s
on
			p.provider_city || dac2.city_name = dt.way_key)
	where
		delay > 0
		and provider_city != city_name)
group by
	provider_name
order by
	average_delay desc
limit 5;
""", nativeQuery = true)
    List<Object[]> getMostDelayedProviders();
}
