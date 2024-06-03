package com.project.billing.Controller;



import com.project.billing.Dto.Bill;
import com.project.billing.Dto.ResponseStructure;
import com.project.billing.Service.BillService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/save")
    public ResponseEntity<ResponseStructure<Bill>> saveBill(@RequestBody Bill bill) {
        return billService.save(bill);
    }

    public ResponseEntity<ResponseStructure<Page<Bill>>> findByLocalDateTimeBetween(LocalDate startDate, LocalDate endDate, int offset, int pageSize, String field){
        return billService.findByLocalDateBetween(startDate, endDate, offset, pageSize, field);
    }

    @GetMapping("/total-revenue")
    public ResponseEntity<ResponseStructure<Double>> totalRevenue() {
        return billService.totalRevenue();
    }

    @GetMapping("/revenue-between")
    public ResponseEntity<ResponseStructure<Double>> findByRevenueBetweenDate(@RequestParam LocalDate startDate,
                                                                              @RequestParam LocalDate endDate,
                                                                              @RequestParam(defaultValue = "0") int offset,
                                                                              @RequestParam(defaultValue = "5") int pageSize,
                                                                              @RequestParam(defaultValue = "id") String field) {
        return billService.findByRevenueBetweenDate(startDate, endDate, offset, pageSize, field);
    }

    @GetMapping("/revenue/by-date-and-payment-mode")
    public ResponseEntity<ResponseStructure<Double>> findRevenueByDateAndPaymentMode(@RequestParam LocalDate startDate,
                                                                                     @RequestParam LocalDate endDate,
                                                                                     @RequestParam String paymentMode) {
        return billService.revenueBetweenDateAndPaymentMode(startDate, endDate, paymentMode);
    }
    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<ResponseStructure<Bill>> findById(@PathVariable long id) {
        return billService.findById(id);
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<ResponseStructure<Page<Bill>>> getAll(@RequestParam(defaultValue = "0") int offset,
                                                                @RequestParam(defaultValue = "5") int pageSize,
                                                                @RequestParam(defaultValue = "id") String field) {
        return billService.findAll(offset, pageSize, field);
    }

    @GetMapping(value = "/findByDateBetween")
    public ResponseEntity<ResponseStructure<Page<Bill>>> findBylocalDateTimeBetween(@RequestParam LocalDate startDate,
                                                                                    @RequestParam LocalDate endDate,
                                                                                    @RequestParam(defaultValue = "0") int offset,
                                                                                    @RequestParam(defaultValue = "5") int pageSize,
                                                                                    @RequestParam(defaultValue = "id") String field) {
        return billService.findByLocalDateBetween(startDate, endDate, offset, pageSize, field);
    }
}
