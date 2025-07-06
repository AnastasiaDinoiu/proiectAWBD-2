package com.library.book.service;

import com.library.book.entity.Publisher;
import com.library.book.exception.ResourceNotFoundException;
import com.library.book.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherService {

    private static final Logger log = LoggerFactory.getLogger(PublisherService.class);
    private final PublisherRepository publisherRepository;

    public List<Publisher> findAll() {
        log.debug("Finding all publishers");
        return publisherRepository.findAll();
    }

    public Page<Publisher> findAll(Pageable pageable) {
        log.debug("Finding all publishers with pagination");
        return publisherRepository.findAll(pageable);
    }

    public Publisher findById(Long id) {
        log.debug("Finding publisher by id: {}", id);
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id));
    }

    public Publisher save(Publisher publisher) {
        log.debug("Saving publisher: {}", publisher.getName());
        return publisherRepository.save(publisher);
    }

    public Publisher update(Long id, Publisher publisher) {
        log.debug("Updating publisher with id: {}", id);
        Publisher existingPublisher = findById(id);

        existingPublisher.setName(publisher.getName());
        existingPublisher.setAddress(publisher.getAddress());
        existingPublisher.setPhoneNumber(publisher.getPhoneNumber());
        existingPublisher.setEmail(publisher.getEmail());
        existingPublisher.setWebsite(publisher.getWebsite());

        return publisherRepository.save(existingPublisher);
    }

    public void deleteById(Long id) {
        log.debug("Deleting publisher with id: {}", id);
        Publisher publisher = findById(id);

        if (!publisher.getBooks().isEmpty()) {
            throw new IllegalStateException("Cannot delete publisher with associated books");
        }

        publisherRepository.delete(publisher);
    }

    public Page<Publisher> searchByName(String name, Pageable pageable) {
        log.debug("Searching publishers by name: {}", name);
        return publisherRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}