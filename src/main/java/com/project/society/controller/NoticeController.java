package com.project.society.controller;

import com.project.society.model.Notice;
import com.project.society.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {
    @Autowired
    private NoticeService service;

    @PostMapping
    public Notice create(@RequestBody Notice n){ return service.createNotice(n); }

    @GetMapping
    public List<Notice> get(@RequestParam String societyId, @RequestParam List<String> roles){
        return service.getNotices(societyId, roles);
    }

    @PutMapping("/{id}") public Notice update(@PathVariable String id,@RequestBody Notice n){ return service.updateNotice(id,n); }

    @DeleteMapping("/{id}") public void delete(@PathVariable String id){ service.deleteNotice(id); }
}
