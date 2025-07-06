package com.library.book.controller;

import com.library.book.dto.PublisherDTO;
import com.library.book.entity.Publisher;
import com.library.book.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private static final Logger log = LoggerFactory.getLogger(PublisherController.class);
    private final PublisherService publisherService;

    @GetMapping
    public Page<PublisherDTO> getAllPublishers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String search) {

        log.info("Getting publishers - page: {}, size: {}, search: {}", page, size, search);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Publisher> publisherPage;
        if (search != null && !search.isEmpty()) {
            publisherPage = publisherService.searchByName(search.trim(), pageRequest);
        } else {
            publisherPage = publisherService.findAll(pageRequest);
        }

        return publisherPage.map(this::convertToDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisherById(@PathVariable Long id) {
        log.info("Getting publisher with id: {}", id);

        try {
            Publisher publisher = publisherService.findById(id);
            return ResponseEntity.ok(convertToDTO(publisher));
        } catch (Exception e) {
            log.error("Publisher not found with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PublisherDTO> createPublisher(@Valid @RequestBody PublisherDTO publisherDTO) {
        log.info("Creating new publisher: {}", publisherDTO.getName());

        try {
            Publisher publisher = convertToEntity(publisherDTO);
            Publisher savedPublisher = publisherService.save(publisher);
            return ResponseEntity.ok(convertToDTO(savedPublisher));
        } catch (Exception e) {
            log.error("Error creating publisher", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO> updatePublisher(@PathVariable Long id, @Valid @RequestBody PublisherDTO publisherDTO) {
        log.info("Updating publisher: {}", id);

        try {
            Publisher publisher = convertToEntity(publisherDTO);
            publisher.setId(id);
            Publisher updatedPublisher = publisherService.update(id, publisher);
            return ResponseEntity.ok(convertToDTO(updatedPublisher));
        } catch (Exception e) {
            log.error("Error updating publisher", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        log.info("Deleting publisher: {}", id);

        try {
            publisherService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting publisher", e);
            return ResponseEntity.badRequest().build();
        }
    }

    private PublisherDTO convertToDTO(Publisher publisher) {
        PublisherDTO dto = new PublisherDTO();
        dto.setId(publisher.getId());
        dto.setName(publisher.getName());
        dto.setAddress(publisher.getAddress());
        dto.setPhoneNumber(publisher.getPhoneNumber());
        dto.setEmail(publisher.getEmail());
        dto.setWebsite(publisher.getWebsite());
        return dto;
    }

    private Publisher convertToEntity(PublisherDTO dto) {
        Publisher publisher = new Publisher();
        publisher.setId(dto.getId());
        publisher.setName(dto.getName());
        publisher.setAddress(dto.getAddress());
        publisher.setPhoneNumber(dto.getPhoneNumber());
        publisher.setEmail(dto.getEmail());
        publisher.setWebsite(dto.getWebsite());
        return publisher;
    }
}