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

    @Query(value = "select * from orders where guest_id = ?1 and state = ?2 and odate = (select MAX(odate) from orders where guest_id = ?1 and state = ?2)", nativeQuery = true)
    List<Order> findLatest(String guestId, String state);

    @Query(value = "SELECT o.odate as date, o.state, o.guest_id, g.name as guest_name, o.product_id, " +
            "p.name as product_name, c.name as product_category, p.unit as product_unit, o.price as product_price, " +
            "o.num as product_num, o.amount as product_amount, p.imgfile as product_img, o.note " +
            "FROM orders o LEFT JOIN product p ON o.product_id = p.id LEFT JOIN guest g on o.guest_id = g.id " +
            "LEFT JOIN category c ON c.code= p.p_code WHERE o.guest_id=?1 AND o.odate=?2 " +
            "AND o.state=?3 ORDER BY p.p_code", nativeQuery = true)
    List<Object[]> findAllWith(String guestId, Date date, String state);
}
