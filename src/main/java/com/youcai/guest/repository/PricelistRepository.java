package com.youcai.guest.repository;

import com.youcai.guest.dataobject.Pricelist;
import com.youcai.guest.dataobject.PricelistKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

public interface PricelistRepository extends JpaRepository<Pricelist, PricelistKey> {
    List<Pricelist> findByIdGuestIdAndIdPdate(String guestId, Date pdate);

    @Query(value = "select distinct pdate from pricelist where guest_id = ?1 order by pdate desc", nativeQuery = true)
    List<Date> findDistinctId_PdateById_GuestId(String guestId);

    @Query(value = "SELECT pri.pdate as date, pri.guest_id, g.name as guest_name, pri.product_id, " +
            "pro.name as product_name, c.name as product_category, pro.unit as product_unit, " +
            "pro.price as marketPrice, pri.price as guestPrice, pro.imgfile as product_img, " +
            "pri.note FROM pricelist pri LEFT JOIN product pro ON pri.product_id = pro.id " +
            "LEFT JOIN guest g on pri.guest_id = g.id LEFT JOIN category c ON c.code= pro.p_code " +
            "WHERE pri.guest_id=?1 AND pri.pdate=?2 ORDER BY pro.p_code", nativeQuery = true)
    List<Object[]> findAllWith(String guestId, Date date);
}
