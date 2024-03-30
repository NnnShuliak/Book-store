package org.example.controller;



import lombok.RequiredArgsConstructor;
import org.example.dto.BookDTO;
import org.example.service.Impl.BookServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookServiceImpl bookService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(bookService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findBook(@PathVariable String id){

        return ResponseEntity.ok(bookService.findByIsbn(id));
    }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookDTO bookDTO){
        return ResponseEntity.ok(bookService.create(bookDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable String id,
            @RequestBody BookDTO bookDTO
    ) {
        return ResponseEntity.ok(bookService.update(id, bookDTO));
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        bookService.deleteById(id);
    }



}
