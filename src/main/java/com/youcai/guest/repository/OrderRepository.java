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

    @Query(value = "select distinct state from orders where guest_id = ?1 and odate = ?2 order by state asc", nativeQuery = true)
    List<String> findDistinctIdStateByIdGuestIdAndIdOdate(String guestId, Date date);

    List<Order> findByIdGuestIdAndIdOdateAndIdState(String guestId, Date date, String state);

    @Modifying
    @Query(value = "update orders set state = ?3 where guest_id = ?1 and odate = ?2 and state = ?4", nativeQuery = true)
    void back(String guestId, Date date, String state, String oldState);

    @Query(value = "select 1 from orders where guest_id = ?1 and odate = ?2 and state in ?3 limit 1", nativeQuery = true)
    String find(String guestId, Date date, List<String> states);
}
