package com.youcai.guest.repository;

import com.youcai.guest.dataobject.Order;
import com.youcai.guest.dataobject.OrderKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, OrderKey> {
    @Query(value = "select distinct odate from orders where guest_id = ?1 order by odate desc", nativeQuery = true)
    List<Date> findDistinctIdOdateByIdGuestId(String guestId);

    List<Order> findByIdGuestIdAndIdOdate(String guestId, Date date);

    @Modifying
    @Query(value = "delete from orders where guest_id=?1 and odate=?2", nativeQuery = true)
    void deleteByIdGuestIdAndIdOdate(String guestId, Date date);
}
