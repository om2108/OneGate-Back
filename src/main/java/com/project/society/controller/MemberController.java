package com.project.society.controller;

import com.project.society.model.Member;
import com.project.society.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService service;

    // Get all members of a society
    @GetMapping
    public List<Member> getMembers(@RequestParam String societyId){
        return service.getMembersBySociety(societyId);
    }

    // Add a member
    @PostMapping
    public Member addMember(@RequestBody Member member){
        return service.addMember(member);
    }

    // Update role of a member
    @PutMapping("/{id}/role")
    public Member updateRole(@PathVariable String id, @RequestParam String role){
        return service.updateRole(id, role);
    }

    // Delete a member
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable String id){
        service.deleteMember(id);
    }
}
