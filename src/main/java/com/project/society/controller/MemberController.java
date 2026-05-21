package com.project.society.controller;

import com.project.society.model.Member;
import com.project.society.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService service;

    // =====================================
    // GET MEMBERS BY SOCIETY
    // =====================================

    @GetMapping
    public List<Member> getMembers(
            @RequestParam String societyId
    ) {

        return service.getMembersBySociety(
                societyId
        );
    }

    // =====================================
    // GET MEMBER BY USER ID
    // =====================================

    @GetMapping("/user/{userId}")
    public Member getByUserId(
            @PathVariable String userId
    ){

        return service.getByUserId(
                userId
        );
    }

    // =====================================
    // ADD MEMBER
    // =====================================

    @PostMapping
    public Member addMember(
            @RequestBody Member member
    ) {

        return service.addMember(member);
    }

    // =====================================
    // UPDATE ROLE + PROPERTY
    // =====================================

    @PutMapping("/{id}/role")
    public Member updateRole(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {

        String role =
                body.get("role");

        String propertyId =
                body.get("propertyId");

        return service.updateRole(
                id,
                role,
                propertyId
        );
    }

    // =====================================
    // DELETE MEMBER
    // =====================================

    @DeleteMapping("/{id}")
    public void deleteMember(
            @PathVariable String id
    ) {

        service.deleteMember(id);
    }
}