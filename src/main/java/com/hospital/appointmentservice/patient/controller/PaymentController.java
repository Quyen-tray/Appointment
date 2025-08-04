package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.config.VNPayConfig;
import com.hospital.appointmentservice.patient.dto.*;
import com.hospital.appointmentservice.patient.entity.Invoice;
import com.hospital.appointmentservice.patient.entity.Payment;
import com.hospital.appointmentservice.patient.service.InvoiceService;
import com.hospital.appointmentservice.patient.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private final InvoiceService invoiceService;

    @Autowired
    private final PaymentService paymentService;


    @PostMapping("/{appointmentId}")
    public ResponseEntity<?> createInvoice(@PathVariable("appointmentId") UUID appointmentId) {
        try {
             invoiceService.addNewInvoice(appointmentId);

            return ResponseEntity.status(HttpStatus.OK).body("Create invoice successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get invoice process" + e.getMessage());
        }

    }

    @PostMapping("/send-email")
    public ResponseEntity<?> snenEmail(@RequestBody NotifyUnpaidDto invoices) {
        try {
            invoiceService.sendEmail(invoices);
            return ResponseEntity.status(HttpStatus.OK).body("Send email successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get invoice process" + e.getMessage());
        }
    }

    @GetMapping("/invoices/unpaid")
    public ResponseEntity<?> getInvoiceUnpaid() {
        List<UnpaidInvoiceDto> invoices = invoiceService.getAllUnpaidInvoice();
        if (invoices == null || invoices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    }

    // Endpoint xử lý thông tin giao dịch sau khi thanh toán
    @GetMapping("/check/return")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "vnp_Amount", required = false) String amount,
            @RequestParam(value = "vnp_BankCode", required = false) String bankCode,
            @RequestParam(value = "vnp_BankTranNo",  required = false) String bankTranNo,
            @RequestParam(value = "vnp_CardType" , required = false) String cardType,
            @RequestParam(value = "vnp_OrderInfo", required = false) String orderInfo,
            @RequestParam(value = "vnp_PayDate", required = false) String payDate,
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
            @RequestParam(value = "vnp_TmnCode", required = false) String tmnCode,
            @RequestParam(value = "vnp_TransactionNo", required = false) String transactionNo,
            @RequestParam(value = "vnp_TransactionStatus", required = false) String transactionStatus,
            @RequestParam(value = "vnp_TxnRef", required = false) String txnRef,
            @RequestParam(value = "vnp_SecureHash", required = false) String secureHash

    ) throws UnsupportedEncodingException {

        // CHia lại giá tiền
        int price = Integer.parseInt(amount);

        System.out.println(URLDecoder.decode(orderInfo, StandardCharsets.US_ASCII.toString()));
        // Check sum
        Map fields = new HashMap<>();
        fields.put("vnp_Amount" , amount);
        fields.put("vnp_BankCode" , bankCode);
        fields.put("vnp_BankTranNo" , bankTranNo);
        fields.put("vnp_CardType" , cardType);
        fields.put("vnp_OrderInfo" , "OrderInfo");
        fields.put("vnp_PayDate" , payDate);
        fields.put("vnp_ResponseCode" , responseCode);
        fields.put("vnp_TmnCode" , tmnCode);
        fields.put("vnp_TransactionNo" , transactionNo);
        fields.put("vnp_TransactionStatus" , transactionStatus);
        fields.put("vnp_TxnRef" , txnRef);
        fields.put("vnp_SecureHash" , secureHash);

        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPayConfig.hashAllFields(fields);
        System.out.println(signValue);

        // Tạo đối tượng TransactionResponse để trả về kết quả giao dịch
        paymentService.updatePayment(UUID.fromString(txnRef));
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:3000/patient/payments"))
                .build();

    }

    // tạo thanh toán vnpay
    @PostMapping("/add/transaction/{invoiceId}")
    public ResponseEntity<PaymentResponse> addNewPaymentTransaction(
            @PathVariable UUID invoiceId
    ) {
        try {
            Invoice invoice = invoiceService.getInvoiceById(invoiceId);
            Payment payment = paymentService.addNewPayment(invoiceId);
            PaymentResponse paymentResponse = paymentService.createNewVnPayPayment(invoice.getAmount(), payment.getId());
            if (paymentResponse != null) {
                return ResponseEntity.status(HttpStatus.OK).body(paymentResponse);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
