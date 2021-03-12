package com.example.demo.controllers;

import com.example.demo.entities.Invoice;
import com.example.demo.services.InvoiceService;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
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

    @GetMapping("/list")
    public List<Invoice> getInvoices(@RequestParam Integer pageNumber) {
        return invoiceService.getPaginatedInvoices( pageNumber);
    }

    @GetMapping("/search")
    public List<Invoice> searchInvoices(@RequestParam String keyword, @RequestParam Integer pageNumber) {
        return invoiceService.searchInvoice( keyword, pageNumber);
    }

    @GetMapping("/view/{id}")
    public Invoice getInvoice(@PathVariable Integer id) {

        return invoiceService.getInvoice( id);
    }



    @PutMapping("/create")
    public void createInvoice(@RequestBody Invoice invoice) {
        invoiceService.createInvoice(invoice);
    }

    @PostMapping("/update")
    public void updateInvoice(@RequestBody Invoice invoice) {
        invoiceService.updateInvoice(invoice);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteInvoice(@PathVariable Integer id) {
        invoiceService.deleteInvoice(id);
    }


    @PostMapping(value = "/image/{invoiceId}")
    Integer uploadImage(@RequestParam MultipartFile image, @PathVariable Integer invoiceId) throws Exception {

        return invoiceService.save(image.getBytes(), image.getOriginalFilename(), invoiceId);
    }

    @GetMapping(value = "/image/{invoiceId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    FileSystemResource downloadImage(@PathVariable Integer invoiceId) throws Exception {
        return invoiceService.find(invoiceId);
    }

    @GetMapping(value = "/pdf/{invoiceId}", produces={"application/pdf"})
    FileSystemResource downloadPdf(@PathVariable Integer invoiceId) throws Exception {
        return invoiceService.find(invoiceId);
    }


}
