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

        this.service =
                service;

    }

    @PostMapping

    public VisitorEntry add(

            @RequestBody
            VisitorEntry visitor

    ){

        return service
                .addVisitor(
                        visitor
                );

    }

    @PostMapping("/regular")

    public VisitorEntry createRegular(

            @RequestBody
            VisitorEntry visitor

    ){

        return service
                .createRegularVisitor(
                        visitor
                );

    }

    @GetMapping("/secretary")

    public List<VisitorEntry>
    pending(

            @RequestParam
            String societyId

    ){

        return service
                .secretaryQueue(
                        societyId
                );

    }

    @GetMapping

    public List<VisitorEntry>
    all(

            @RequestParam
            String societyId

    ){

        return service
                .all(
                        societyId
                );

    }

    @PutMapping(
            "/{id}/approve"
    )

    public VisitorEntry approve(

            @PathVariable
            String id

    ){

        return service
                .approve(
                        id
                );

    }

    @PutMapping(
            "/{id}/reject"
    )

    public VisitorEntry reject(

            @PathVariable
            String id

    ){

        return service
                .reject(
                        id
                );

    }

    @PutMapping(
            "/{id}/checkin"
    )

    public VisitorEntry checkIn(

            @PathVariable
            String id

    ){

        return service
                .checkIn(
                        id
                );

    }

    @PutMapping(
            "/{id}/checkout"
    )

    public VisitorEntry checkOut(

            @PathVariable
            String id

    ){

        return service
                .checkOut(
                        id
                );

    }

}