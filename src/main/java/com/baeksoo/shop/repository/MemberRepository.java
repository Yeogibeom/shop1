package com.baeksoo.shop.repository;

import com.baeksoo.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface   MemberRepository  extends JpaRepository<Member, Long> {
}
