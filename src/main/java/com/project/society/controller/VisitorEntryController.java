package com.project.society.controller;

import com.project.society.model.VisitorEntry;

import com.project.society.service.VisitorEntryService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/visitors")

@CrossOrigin("*")

public class VisitorEntryController {

    private final VisitorEntryService service;

    public VisitorEntryController(
            VisitorEntryService service
    ){

        this.service=
                service;

    }


    @PostMapping
    public VisitorEntry add(
            @RequestBody VisitorEntry v
    ){

        return service
                .addVisitor(v);

    }


    @GetMapping("/secretary")
    public List<VisitorEntry>
    secretary(

            @RequestParam
            String societyId

    ){

        return service
                .secretaryQueue(
                        societyId
                );

    }


    @GetMapping("/member")
    public List<VisitorEntry>
    member(

            @RequestParam
            String societyId,

            @RequestParam
            String memberId

    ){

        return service
                .memberQueue(
                        societyId,
                        memberId
                );

    }


    @PutMapping("/{id}/approve")
    public VisitorEntry approve(

            @PathVariable String id,

            @RequestParam String role

    ){

        return service
                .approve(
                        id,
                        role
                );

    }


    @PutMapping("/{id}/reject")
    public VisitorEntry reject(

            @PathVariable String id,

            @RequestParam String role

    ){

        return service
                .reject(
                        id,
                        role
                );

    }


    @PutMapping("/{id}/forward")
    public VisitorEntry forward(

            @PathVariable String id,

            @RequestParam String memberId

    ){

        return service
                .forward(
                        id,
                        memberId
                );

    }

}