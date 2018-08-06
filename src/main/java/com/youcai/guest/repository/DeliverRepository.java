package com.youcai.guest.repository;

import com.youcai.guest.dataobject.DeliverList;
import com.youcai.guest.dataobject.DeliverListKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DeliverRepository extends JpaRepository<DeliverList, DeliverListKey> {
    @Query(value = "select distinct order_date from d_list where guest_id = ?1 order by order_date desc", nativeQuery = true)
    List<Date> findDistinctIdDdateByIdGuestId(String guestId);

    @Query(value = "select distinct order_date,state from d_list where guest_id = ?1 order by order_date desc", nativeQuery = true)
    List<Object[]> findDistinctDateAndStateByGuestId(String guestId);

    List<DeliverList> findByIdGuestIdAndIdOrderDate(String guestId, Date date);

    @Modifying
    @Query(value = "update d_list set state=?4 where guest_id=?1 and order_date=?2 and state=?3", nativeQuery = true)
    void updateState(String guestId, Date date, String oldState, String newState);

    @Query(value = "SELECT\n" +
            "\td.ddate AS deliverDate,\n" +
            "\td.order_date AS orderDate,\n" +
            "\td.state,\n" +
            "\tdriver.id AS driver_id,\n" +
            "\tdriver.NAME AS driver_name,\n" +
            "\td.guest_id,\n" +
            "\tg.NAME AS guest_name,\n" +
            "\td.product_id,\n" +
            "\tp.NAME AS product_name,\n" +
            "\tc.NAME AS product_category,\n" +
            "\tp.unit AS product_unit,\n" +
            "\td.price AS product_price,\n" +
            "\td.num AS product_num,\n" +
            "\td.amount AS product_amount,\n" +
            "\tp.imgfile AS product_img,\n" +
            "\td.note \n" +
            "FROM\n" +
            "\td_list d\n" +
            "\tLEFT JOIN product p ON d.product_id = p.id\n" +
            "\tLEFT JOIN guest g ON d.guest_id = g.id\n" +
            "\tLEFT JOIN category c ON c.CODE = p.p_code\n" +
            "\tLEFT JOIN driver ON driver.id = d.d_id \n" +
            "WHERE\n" +
            "\td.guest_id = ?1 \n" +
            "\tAND d.order_date = ?2 \n" +
            "ORDER BY\n" +
            "\tp.p_code", nativeQuery = true)
    List<Object[]> findAllWith(String guestId, Date date);
}
