package com.fds.payflow.repository;

import com.fds.payflow.vo.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByOrderByRecordTimeDesc();
}
