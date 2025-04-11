package com.bubusyaka.demo.configuration;

import com.bubusyaka.demo.model.entity.*;
import com.bubusyaka.demo.model.enums.Role;
import com.bubusyaka.demo.repository.jpa.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class DatabaseInitializer {

    private final ItemRepository itemRepository;
    private final ItemTestRepository itemTestRepository;
    private final ProviderRepository providerRepository;
    private final AllowedCitiesRepository allowedCitiesRepository;
    private final DeliveryTimeRepository deliveryTimeRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

//    @PostConstruct
//    public void initNewColumnsOrders() {
//        var order = new OrderEntity();
//        for (ProductCategories productCategories : ProductCategories.values()) {
//            order.setProductCategory(productCategories.name());
//        }
//        for (AgeCategories ageCategories : AgeCategories.values()) {
//            order.setAgeCategory(ageCategories.getAge());
//        }
//
//        try {
//            orderRepository.save(order);
//        }
//        catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }


    //@PostConstruct
    public void initUsers(){
        var idValue=0;
        for (UsersName usersName : UsersName.values()) {
            var user = new UserEntity();
            idValue += 1;
            user.setId((long) idValue);
            user.setUsername(usersName.name());
            user.setAge(new Random().nextLong(60) + 16);
            List<City> enumValues = Arrays.asList(City.values());
            user.setCity(String.valueOf(enumValues.get(new Random().nextInt(enumValues.size()))));
            var role = Role.values()[new Random().nextInt(Role.values().length)];
            user.setRole(role);

            try {
                userRepository.save(user);
            }
            catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
    //@PostConstruct
    public void initDB() {
        for (City cityName : City.values()) {
            var city = new DeliveryAllowedCityEntity();
            city.setCityName(cityName.name());
            try {
                allowedCitiesRepository.save(city);
            }
            catch (Exception e) {}
        }

        Map.of(Provider.Ozon, City.MOSCOW,
                        Provider.Wildberries, City.SAINT_PETERSBURG,
                        Provider.Yandex, City.CHELYABINSK,
                        Provider.Sbermarket, City.VOLOGDA,
                        Provider.Kazanexpress, City.SOCHI,
                        Provider.Asos, City.PARIS,
                        Provider.Amazon, City.NEW_YORK,
                        Provider.Pochta, City.UFA,
                        Provider.Ebay, City.LA,
                        Provider.Aliexpress, City.PEKIN
                )
                .forEach((providerType, providerCity) -> {
                    var provider = new ProviderEntity();
                    provider.setId(provider.getId());
                    provider.setProviderName(providerType.name());
                    provider.setProviderCity(providerCity.name());
                    try {
                        providerRepository.save(provider);
                    }
                    catch (Exception e) {}
                });

        for (int i = 0; i < 100; i++){
            var item = new ItemEntity();
            item.setName("Item-" + RandomStringUtils.randomAlphanumeric(2));
            item.setPrice(new Random().nextLong(200000));
            item.setProviderId(new Random().nextLong(Provider.values().length - 1) + 1);

            itemRepository.save(item);
        }


        Map.of("macbook", Provider.Yandex,
                        "ipad", Provider.Yandex,
                        "applewatch", Provider.Ozon,
                        "iphone", Provider.Ozon,
                        "samsa", Provider.Kazanexpress,
                        "dress", Provider.Kazanexpress,
                        "guccibag", Provider.Sbermarket,
                        "musyacat", Provider.Wildberries,
                        "chair", Provider.Wildberries
                )
                .forEach((itemName, providerType) -> {
                    var item = new ItemEntity();
                    item.setName(itemName);
                    item.setPrice(new Random().nextLong(200000));
                    item.setProviderId((long) providerType.getId());
                    //itemRepository.save(item);
                });

        for (City cityFrom : City.values()) {
            for (City cityTo : City.values()) {
                var deliveryTime = new DeliveryTimeEntity();
                deliveryTime.setWayKey(cityFrom.name() + cityTo.name());
                if (!cityFrom.equals(cityTo)) {
                    deliveryTime.setEstimatedDays(new Random().nextInt(7) + 1);
                } else {
                    deliveryTime.setEstimatedDays(1);
                }
                deliveryTimeRepository.save(deliveryTime);
            }
        }

        for (int i = 0; i < 100; i++) {
            var order = new OrderEntity();
            order.setItemId(new Random().nextLong(8) + 1);
            order.setCityId(new Random().nextLong(4) + 1);
            order.setCreationDate(LocalDateTime.now().minusDays(new Random().nextInt(7) + 1));
            order.setCompletionDate(LocalDateTime.now());
            order.setIsCompleted(true);
            //orderRepository.save(order);
        }

        /*//itemRepository.deleteAll();
        var time = System.currentTimeMillis();
        var exec = Executors.newFixedThreadPool(10);
        var futures = new CompletableFuture[3];
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            List<ItemEntity> items = new ArrayList<>();
            futures[i] = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < 100000; j++) {
                    var itemEntity = new ItemEntity();
                    itemEntity.setName("Chepushila" + finalI + "_" + j);
                    itemEntity.setPrice((long) j);
                    //itemEntity.setProviderId((long) j);
                    items.add(itemEntity);
                }
                itemRepository.saveAll(items);
            }, exec);
        }
        //CompletableFuture.allOf(futures).join();
        log.info("Database initialization completed");
        log.info("Time to load: " + (System.currentTimeMillis() - time));*/
    }

    public enum ProductCategories {
        ChildrenToys,
        Clothes,
        Medicine,
        AdultToys,
        HouseholdGoods,
        Cosmetology,
        Electronics,
        Maintenance,
        PetSupplies,
        FoodSupplies
    }

    @RequiredArgsConstructor
    private enum AgeCategories {
        THREE_PLUS(3),
        SIX_PLUS(6),
        TWELVE_PLUS(12),
        EIGHTEEN_PLUS(18),
        THIRTYFIVE_PLUS(35),
        FIFTYFIVE_PLUS(50);

        private final long age;

        public long getAge() {
            return age;
        }
    }

    public enum City {
        MOSCOW,
        SAINT_PETERSBURG,
        VOLOGDA,
        CHELYABINSK,
        SOCHI,
        PARIS,
        NEW_YORK,
        UFA,
        LA,
        PEKIN
    }

    public enum UsersName {
        ivanivanov,
        petrpetrov,
        sidrsidrov,
        adminadmin,
        bubabubovich,
        bubabubovna,
        grandmaster,
        umpalumpa,
        zuzyabuba,
        victorvictor
    }

    @RequiredArgsConstructor
    @Getter
    enum Provider {
        Ozon(1),
        Wildberries(2),
        Yandex(3),
        Sbermarket(4),
        Kazanexpress(5),
        Asos(6),
        Amazon(7),
        Pochta(8),
        Ebay(9),
        Aliexpress(10);

        private final int id;
    }
}

