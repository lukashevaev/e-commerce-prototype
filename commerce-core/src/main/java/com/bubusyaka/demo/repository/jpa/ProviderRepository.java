package com.bubusyaka.demo.repository.jpa;

import com.bubusyaka.demo.model.entity.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("PROVIDER")
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {

    @Query(value = """
                    SELECT i.id,p.provider_city 
                       FROM item i JOIN providers p 
                        ON i.provider_id = p.id
                        """, nativeQuery = true)
    List<Object[]> findProvidersForItems();



    @Query(value = """
SELECT provider_name from providers p
where p.id = :pId
""", nativeQuery = true)
    List<Object[]> findProviderByProviderId(@Param("pId") Long providerId);
}
