package com.project.billing.Dao;


import com.project.billing.Dto.Bill;
import com.project.billing.Repository.BillRepository;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class BillDao {

    @Autowired
    private BillRepository billRepository;
    public Bill findByCustPhone(String phone){
        return billRepository.findBycustPhone(phone);
    }

    public Bill findByCustEmail(String email){
        return billRepository.findByCustEmail(email);
    }

    public Bill save(Bill bill){
        return billRepository.save(bill);}

    public void delete(Bill bill){
        billRepository.delete(bill);
    }

    public Bill findById(long id){
        Optional<Bill> optional = billRepository.findById(id);
        return optional.orElse(null);}

    public Page<Bill> findAll(int offset, int pageSize, String field){
        return billRepository.findAll(PageRequest.of(offset,pageSize).withSort(Sort.by(field).descending()));}

    public Page<Bill> findByLocalDateTimeBetween(LocalDate startDate, LocalDate endDate, int offset, int pageSize, String field) {
        return billRepository.findBylocalDateBetween(startDate, endDate, PageRequest.of(offset, pageSize).withSort(Sort.by(field).descending()));
    }
}
