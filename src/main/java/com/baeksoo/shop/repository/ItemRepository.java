package com.baeksoo.shop.repository;

import com.baeksoo.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

     Page<Item> findPageBy(Pageable page);
     List<Item> findAllBytitle(String searchText);

     List<Item> findAllBytitleContains(String title);

     @Query(value = "SELECT * FROM item WHERE MATCH(title) AGAINST (?1 IN NATURAL LANGUAGE MODE)", nativeQuery = true)
     List<Item> rawQuery1(String test);
}
