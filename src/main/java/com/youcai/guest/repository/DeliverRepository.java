package com.youcai.guest.repository;

import com.youcai.guest.dataobject.DeliverList;
import com.youcai.guest.dataobject.DeliverListKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DeliverRepository extends JpaRepository<DeliverList, DeliverListKey> {
    @Query(value = "select distinct ddate from d_list where guest_id = ?1 order by ddate desc", nativeQuery = true)
    List<Date> findDistinctIdDdateByIdGuestId(String guestId);

    List<DeliverList> findByIdGuestIdAndIdDdateAndIdDriverId(String guestId, Date date, String driverId);
    List<DeliverList> findByIdGuestIdAndIdDdate(String guestId, Date date);

    @Modifying
    @Query(value = "update d_list set state=?4 where guest_id=?1 and ddate=?2 and state=?3", nativeQuery = true)
    void updateState(String guestId, Date date, String oldState, String newState);
}
