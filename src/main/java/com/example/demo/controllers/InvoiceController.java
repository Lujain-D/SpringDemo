package com.example.demo.controllers;

import com.example.demo.entities.Invoice;
import com.example.demo.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path= "api/v1/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
//    @CrossOrigin(origins = "http://localhost:8081")
    public List<Invoice> getInvoices() {
        return invoiceService.getInvoices();
    }

    @PutMapping
    public void createInvoice(@RequestBody Invoice invoice) {
        invoiceService.createInvoice(invoice);
    }

    @PostMapping
    public void updateInvoice(@RequestBody Invoice invoice) {
        invoiceService.updateInvoice(invoice);
    }

    @DeleteMapping
    public void deleteInvoice(@PathVariable("id") Integer id) {
        invoiceService.deleteInvoice(id);
    }
}
