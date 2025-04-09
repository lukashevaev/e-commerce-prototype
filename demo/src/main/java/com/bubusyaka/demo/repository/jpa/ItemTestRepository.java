package com.bubusyaka.demo.repository.jpa;


import com.bubusyaka.demo.model.entity.ItemEntity;
import com.bubusyaka.demo.model.entity.ItemEntityTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ITEM_TEST")
public interface ItemTestRepository extends JpaRepository<ItemEntityTest, Long> {

    @Query(value = "SELECT * from item_test i where i.\"name\" = :name", nativeQuery = true)
    List<ItemEntityTest> findByName(@Param("name") String name);

    @Query(value = """
            SELECT i.id, i.name, i.price, i.provider_id from item_test i 
                join providers p on i.provider_id = p.id 
                     where p.provider_city in (:providerCity)
                         """, nativeQuery = true)
    List<ItemEntityTest>findItemsByProviderCityInNative(List<String> providerCity);
}
