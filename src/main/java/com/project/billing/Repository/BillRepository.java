package com.project.billing.Repository;

import com.project.billing.Dto.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    public List<Bill> findByCustNameContaining(String letter);
     
    public Bill findBycustPhone(String phone);

    public Bill findByCustEmail(String email);

    public List<Bill> findByLocalDateBetweenAndPaymentMode(LocalDate startDate, LocalDate endDate, String paymentMode);

    public Page<Bill> findBylocalDateBetween(LocalDate startDate, LocalDate endDate, PageRequest pageRequest);

    public List<Bill> findByPaymentMode(String paymentMode);

    public List<Bill> findAll();
       @Query("SELECT SUM(b.totalAmount) FROM Bill b")
    Optional<Double> sumTotalAmount();
}
