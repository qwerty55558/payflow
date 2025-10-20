package com.fds.payflow.repository;

import com.fds.payflow.vo.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Integer> {
}
