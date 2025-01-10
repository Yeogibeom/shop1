package com.baeksoo.shop.repository;

import com.baeksoo.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;


public interface ItemRepository extends JpaRepository<Item, Long> {

     Page<Item> findPageBy(PageRequest page);

}
