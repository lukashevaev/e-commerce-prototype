package com.bubusyaka.recommendation.repository;

import com.bubusyaka.recommendation.model.dto.CompositeDTO;
import com.bubusyaka.recommendation.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(value = """
SELECT NEW com.bubusyaka.recommendation.model.dto.CompositeDTO(o.id, o.itemId, i.name, o.userId, i.productCategory, i.ageCategory, ut.age) from OrderEntity o
join ItemEntity i \s
on o.itemId = i.id \s
join UserEntity ut \s
on o.userId = ut.id \s
order by o.completionDate desc
""")
    List<CompositeDTO> findItemsInfoOrderedByUsersNoID();

}
