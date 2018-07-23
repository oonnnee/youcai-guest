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


}
