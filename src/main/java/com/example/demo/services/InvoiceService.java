package com.example.demo.services;

import com.example.demo.entities.Invoice;
import com.example.demo.entities.Item;
import com.example.demo.entities.Track;
import com.example.demo.repositories.InvoiceRepository;
import com.example.demo.repositories.ItemRepository;
import com.example.demo.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ItemRepository itemRepository;
    private final TrackRepository trackRepository;


    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, ItemRepository itemRepository, TrackRepository trackRepository) {
        this.invoiceRepository = invoiceRepository;
        this.itemRepository = itemRepository;
        this.trackRepository = trackRepository;

    }

    public List<Invoice> getInvoices(){
        return invoiceRepository.findAllInvoicesOnly();
    }

    public void createInvoice(Invoice invoice){
        List<Item> items = invoice.getItems();

        invoiceRepository.save(invoice);
        Integer id = invoice.getId();
        System.out.println(id);

        Float total = invoice.getTotal();
        Float calculatedTotal = 0.0f;

        for (Item item : items) {
            item.setInvoice(invoice);
            itemRepository.save(item);
            calculatedTotal+= (item.getPrice()*item.getCount());
        }

        if (!calculatedTotal.equals(total)){
            invoice.setTotal(calculatedTotal);
            invoiceRepository.save(invoice);
        }

        Track track = new Track(invoice, "created", null);
        trackRepository.save(track);

    }

    public void updateInvoice(Invoice invoice) {
        Optional<Invoice> oldInvoiceOpt = invoiceRepository.findById(invoice.getId());
         if (oldInvoiceOpt.isPresent()){
             Invoice oldInvoice =  oldInvoiceOpt.get();
             List<Item> oldItems = oldInvoice.getItems();
             List<Item> items = invoice.getItems();

             Float total = invoice.getTotal();
             Float calculatedTotal = 0.0f;
             List<Integer> updatedItemIds = new ArrayList<>();

             if (items.size()!=0) {
                 for (Item item : items) {
                     item.setInvoice(invoice);
                     itemRepository.save(item);
                     calculatedTotal+= (item.getPrice()*item.getCount());
                     if (item.getId() != null){
                         updatedItemIds.add(item.getId());
                     }
                 }
             }


             System.out.println("updatedItemIds: " + updatedItemIds);
             System.out.println("oldItems: " + oldItems);
             for (Item oldItem: oldItems){
                 if (!updatedItemIds.contains(oldItem.getId())){
                     System.out.println("delete item: " + oldItem);
                     itemRepository.softDelete(oldItem.getId());
                 }
             }

             if (calculatedTotal!= total){
                 invoice.setTotal(calculatedTotal);
                 invoiceRepository.save(invoice);
             }

             Track track = new Track(invoice, "updated", null);
             trackRepository.save(track);

         }



    }


    public void deleteInvoice(Integer id) {
        Optional<Invoice> toBeDeleted = invoiceRepository.findById(id);
        if (toBeDeleted.isPresent()){
            Invoice invoice = toBeDeleted.get();
            invoiceRepository.softDelete(invoice.getId());

            Track track = new Track(invoice, "deleted", null);
            trackRepository.save(track);
        }
    }
}
