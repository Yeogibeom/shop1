package com.baeksoo.shop.repository;

import com.baeksoo.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long> {


}
