package com.project.billing.Service;

import com.project.billing.Dao.BillDao;
import com.project.billing.Dao.ProductDao;
import com.project.billing.Dto.Bill;
import com.project.billing.Dto.BillProduct;
import com.project.billing.Dto.Product;
import com.project.billing.Dto.ResponseStructure;
import com.project.billing.Repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service

public class BillService {

    @Autowired
    private BillDao billDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private BillRepository billRepository;


    public ResponseEntity<ResponseStructure<Bill>> save(Bill bill) {
        ResponseStructure<Bill> responseStructure = new ResponseStructure<>();
        List<BillProduct> billProducts = bill.getBillProduct();

        if (billProducts != null && !billProducts.isEmpty()) {
            double totalPrice = 0;
            List<BillProduct> billProductList = new ArrayList<>();

            for (BillProduct billProduct : billProducts) {
                Optional<Product> optionalProduct = Optional.ofNullable(productDao.findById(billProduct.getProduct().getId()));

                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    billProduct.setProduct(product);
                    billProduct.setBill(bill);

                    double amount = billProduct.getPrice() * billProduct.getQuantity();
                    double gstAmount = amount * 0.18; // 20% GST
                    billProduct.setGST(gstAmount);
                    amount += gstAmount;

                    totalPrice += amount;
                    billProductList.add(billProduct);
                } else {
                    responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
                    responseStructure.setMessage("Product not found: " + billProduct.getProduct().getId());
                    responseStructure.setData(null);
                    return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
                }
            }

            bill.setBillProduct(billProductList);
            bill.setTotalAmount(totalPrice);
            bill = billDao.save(bill);

            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("Bill saved successfully ");
            responseStructure.setData(bill);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);

        } else {
            responseStructure.setStatus(HttpStatus.CONFLICT.value());
            responseStructure.setMessage("No products in the bill ");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<ResponseStructure<Page<Bill>>> findByLocalDateBetween(LocalDate startDate, LocalDate endDate, int offset, int pageSize, String field) {
        ResponseStructure<Page<Bill>> responseStructure = new ResponseStructure<>();
        Page<Bill> bills = billDao.findByLocalDateTimeBetween(startDate, endDate, offset, pageSize, field);

        if (bills.isEmpty()) {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("No bills found between " + startDate + " and " + endDate);
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("Bills found between " + startDate + " and " + endDate);
            responseStructure.setData(bills);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<Bill>> findById(long id)
    {
        ResponseStructure<Bill> responseStructure = new ResponseStructure<>();
        Bill bill = billDao.findById(id);
        if (Objects.isNull(bill)) {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("Bill Does Not Exists To Be Found By ID ");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        } else {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("BIll Found By ID = " + id);
            responseStructure.setData(bill);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<Page<Bill>>> findAll(int offset, int pageSize, String field)
    {
        ResponseStructure<Page<Bill>> responseStructure = new ResponseStructure<>();
        Page<Bill> billsPage = billDao.findAll(offset, pageSize, field);
        if (!billsPage.isEmpty()) {
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("All Bills found ");
            responseStructure.setData(billsPage);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        } else {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("No Bills Found ");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ResponseStructure<Double>> totalRevenue() {
        ResponseStructure<Double> responseStructure = new ResponseStructure<>();

        Optional<Double> totalRevenueOptional = billRepository.sumTotalAmount();
        if (totalRevenueOptional.isPresent()) {
            double totalRevenue = totalRevenueOptional.get();
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("Total Revenue Of Orbit ");
            responseStructure.setData(totalRevenue);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        } else {
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("No bills found to calculate total revenue");
            responseStructure.setData(null);

            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ResponseStructure<Double>> findByRevenueBetweenDate(LocalDate startDate, LocalDate endDate,int offset, int pageSize, String field)
    {
        ResponseStructure<Double> responseStructure = new ResponseStructure<>();
        Page<Bill> bills = billDao.findByLocalDateTimeBetween(startDate,endDate,offset, pageSize,field);
        double totalRevenue = 0;
        for (Bill bill : bills) {
            totalRevenue += bill.getTotalAmount();
        }
        responseStructure.setStatus(HttpStatus.OK.value());
        responseStructure.setMessage("Total Revenue Of Orbit ");
        responseStructure.setData(totalRevenue);
        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<Double>> revenueBetweenDateAndPaymentMode(LocalDate startDate, LocalDate endDate, String paymentMode)
    {
        ResponseStructure<Double> responseStructure = new ResponseStructure<>();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Bill> bills = (List<Bill>) billRepository.findByLocalDateBetweenAndPaymentMode(startDate, endDate, paymentMode);
        double totalRevenue = bills.stream().mapToDouble(Bill::getTotalAmount).sum();

        responseStructure.setStatus(HttpStatus.OK.value());
        responseStructure.setMessage("Total Revenue Of Orbit between " + startDate + " and " + endDate + " for payment mode: " + paymentMode);
        responseStructure.setData(totalRevenue);
        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<Double>> revenueByPaymentMode(String paymentMode) {
        ResponseStructure<Double> responseStructure = new ResponseStructure<>();
        List<Bill> bills = billRepository.findByPaymentMode(paymentMode);
        double totalRevenue = bills.stream().mapToDouble(Bill::getTotalAmount).sum();

        responseStructure.setStatus(HttpStatus.OK.value());
        responseStructure.setMessage("Total Revenue Of Orbit by Payment Mode: " + paymentMode);
        responseStructure.setData(totalRevenue);
        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
    }
}