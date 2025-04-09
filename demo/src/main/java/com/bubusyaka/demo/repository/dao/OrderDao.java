package com.bubusyaka.demo.repository.dao;

import com.bubusyaka.demo.model.dto.MostPopularOrdersDto;
import com.bubusyaka.demo.model.entity.DeliveryAllowedCityEntity;
import com.bubusyaka.demo.model.entity.ItemEntity;
import com.bubusyaka.demo.model.entity.OrderEntity;
import com.bubusyaka.demo.model.entity.ProviderEntity;
import com.bubusyaka.demo.repository.jpa.ProviderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.lang.ExpressionBuilder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderDao {

    private final EntityManager em;
    private ProviderRepository providerRepository;

    //constructor

    public List<OrderEntity> findOrdersByProviderAndCity(Long itemId, Long cityId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> cq = cb.createQuery(OrderEntity.class);

        Root<OrderEntity> order = cq.from(OrderEntity.class);
        //Root<ItemEntity> item = cq.from(ItemEntity.class);

        var predicates = new ArrayList<Predicate>();
        if (itemId != null) {
            predicates.add(cb.equal(order.get("itemId"), itemId));
        }
        if (cityId != null) {
            predicates.add(cb.equal(order.get("cityId"), cityId));
        }

        cq.where(predicates.toArray(new Predicate[1]));

        TypedQuery<OrderEntity> query = em.createQuery(cq);
        return query.getResultList();
    }

    public List<MostPopularOrdersDto> findMostPopularOrders(LocalDate startDate,
                                                            LocalDate endDate,
                                                            Integer lastNDays,
                                                            Integer limit,
                                                            Long cityId,
                                                            String providerName)
            {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<OrderEntity> order = cq.from(OrderEntity.class);

        var predicates = new ArrayList<Predicate>();
        if (lastNDays != null) {
            predicates.add(
                    cb.between(
                            order.get("completionDate"),
                            Timestamp.valueOf(LocalDateTime.now().minusDays(lastNDays)),
                            Timestamp.valueOf(LocalDateTime.now())));
        } else if (startDate != null && endDate != null) {
            predicates.add(cb.between(order.get("completionDate"), startDate, endDate));
        }

        if (cityId != null) {
            predicates.add(cb.equal(order.get("cityId"), cityId));
        }

        if (providerName != null) {
            //var query = em.createNativeQuery("SELECT o.id from orders o join item i on o.item_id = i.id join providers p on i.provider_id = p.id where p.provider_name = " + providerName);
            var query = em.createNativeQuery("select o.id from orders o join item i on o.item_id = i.id join providers p on i.provider_id = p.id where p.provider_name = " + "\'" + providerName + "\'");

            List<Long> orderIdWithRequiredProviderId = query.getResultList();
            var inPredicate = cb.in(order.get("id"));
            for (Long orderId : orderIdWithRequiredProviderId) {
                inPredicate.value(orderId);
            }
            predicates.add(inPredicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.multiselect(order.get("itemId"), cb.count(order.get("itemId")).alias("itemCount"))
                .groupBy(order.get("itemId"));
        TypedQuery<Object[]> query = em.createQuery(cq).setMaxResults(limit);

        return query.getResultList().stream().map(arr -> new MostPopularOrdersDto((Long) arr[0], (Long) arr[1])).collect(Collectors.toList());
    }

}
