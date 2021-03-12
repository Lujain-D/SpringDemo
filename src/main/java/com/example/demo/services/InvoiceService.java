package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.repositories.*;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ItemRepository itemRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final FileSystemRepository fileSystemRepository;
    private final UserUtil userUtil;
    private int pageSize;

//    @Autowired
//    FileSystemRepository fileSystemRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, ItemRepository itemRepository, TrackRepository trackRepository,UserRepository userRepository, FileSystemRepository fileSystemRepository , UserUtil userUtil) {
        this.invoiceRepository = invoiceRepository;
        this.itemRepository = itemRepository;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.fileSystemRepository = fileSystemRepository;
        this.userUtil = userUtil;
        this.pageSize = 10;

    }

    public Invoice getInvoice(Integer id){
        User user = userUtil.getCurrentUser();
        System.out.println("in get invoice");
        Invoice invoice = invoiceRepository.findById(id).get();

        if(user.equals(invoice.getUser())){
            return invoice;
        }
        return new Invoice();

    }

    public List<Invoice> getPaginatedInvoices( Integer pageNumber){
        User user = userUtil.getCurrentUser();
        Role role = user.getRole();

        //user -
        //super - for everyone
        //=audtor - add, delete, update

        Pageable page = PageRequest.of(pageNumber, this.pageSize, Sort.by(Sort.Direction.DESC, "created"));
        List<Invoice> paginatedInvoices;
        if (role.getRole().equals("USER")){
        paginatedInvoices = invoiceRepository.findAllByUser(user, page);
        } else{
            paginatedInvoices = invoiceRepository.findAllPaginated(page);
        }
        return paginatedInvoices;
    }

    public void createInvoice(Invoice invoice){
        User user = userUtil.getCurrentUser();

        List<Item> items = invoice.getItems();
        invoice.setUser(user);
        invoiceRepository.save(invoice);
        Integer id = invoice.getId();

        // make it transactional
        Float total = invoice.getTotal();
        Float calculatedTotal = 0.0f;

        for (Item item : items) {
            item.setInvoice(invoice);
            itemRepository.save(item);
            calculatedTotal+= (item.getPrice()*item.getCount());
        }


        if (calculatedTotal != 0.0f && !calculatedTotal.equals(total)){
            invoice.setTotal(calculatedTotal);
            invoiceRepository.save(invoice);
        }

        Track track = new Track(invoice, "created", user);
        trackRepository.save(track);

    }


    public void updateInvoice(Invoice invoice) {
        User user = userUtil.getCurrentUser();

        Invoice oldInvoice = invoiceRepository.findById(invoice.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));



         if ((user.getRole().getRole().equals("USER") && user.equals(oldInvoice.getUser()) ) || user.getRole().getRole().equals("SUPERUSER")){
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

             Track track = new Track(invoice, "updated", user);
             trackRepository.save(track);
             // inverse annotation
             // create new service class then inheret
         }

    }


    public void deleteInvoice(Integer id) {
        User user = userUtil.getCurrentUser();
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if ((user.getRole().getRole().equals("USER") && user.equals(invoice.getUser()) ) || user.getRole().getRole().equals("SUPERUSER")){
            invoiceRepository.softDelete(invoice.getId());

            Track track = new Track(invoice, "deleted", user);
            trackRepository.save(track);

        }
    }

//    private User getCurrentUser(){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) auth.getPrincipal();
//
//        Optional<User> opUser =  userRepository.findByUsername(userDetails.getUsername());
//        return opUser.get();
//    }

    public List<Invoice> searchInvoice(String keyword, Integer pageNumber) {

        User user = userUtil.getCurrentUser();
        Role role = user.getRole();
        Pageable page = PageRequest.of(pageNumber, this.pageSize, Sort.by(Sort.Direction.DESC, "created"));

        List<Invoice> paginatedInvoices;
        if (role.getRole().equals("USER")){
            paginatedInvoices = invoiceRepository.search(keyword, user.getId(), page);
        }
        else{
            paginatedInvoices = invoiceRepository.search(keyword, page);
        }
        return paginatedInvoices;
    }

    public Integer save(byte[] bytes, String imageName, Integer invoiceId) throws Exception {
        String location = fileSystemRepository.save(bytes, imageName);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Path newFile = Paths.get(location);
        BasicFileAttributes attr = Files.readAttributes(newFile, BasicFileAttributes.class);

        invoice.setFileLocation(location);
        invoice.setFileSize(attr.size());
        invoice.setFileLastCreated(new Date((attr.creationTime().toMillis())));
        invoice.setFileLastUpdated(new Date((attr.lastModifiedTime().toMillis())));


        return invoiceRepository.save(invoice).getId();
    }

    public FileSystemResource find(Integer invoiceID) {
        Invoice invoice = invoiceRepository.findById(invoiceID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return fileSystemRepository.findInFileSystem(invoice.getFileLocation());
    }




}
