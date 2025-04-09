package com.bubusyaka.demo.repository.jpa;

import com.bubusyaka.demo.model.entity.ItemEntity;
import com.bubusyaka.demo.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ITEM")
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    @Query(value = "SELECT * from item i where i.\"name\" = :name", nativeQuery = true)
    List<ItemEntity> findByName(@Param("name") String name);

    @Query(value = """
            SELECT i.id, i.name, i.price, i.provider_id, i.created_date, i.last_modified_date from item i 
                join providers p on i.provider_id = p.id 
                     where p.provider_city in (:providerCity)
                         """, nativeQuery = true)
    List<ItemEntity>findItemsByProviderCityInNative(List<String> providerCity);


    @Query(value = """
            SELECT NEW com.bubusyaka.demo.model.entity.ItemEntity(i.id, i.name, i.price, i.productCategory,i.ageCategory) FROM ItemEntity i""")
    List<ItemEntity> findItemInformation();

    @Query(value = """
Select NEW com.bubusyaka.demo.model.entity.ItemEntity(i.name) from ItemEntity i 
where i.productCategory in (:productCategory)
""")
    List<ItemEntity> findItemNamesByCategory(List<String> productCategory);
}