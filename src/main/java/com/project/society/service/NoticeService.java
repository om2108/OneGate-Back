package com.project.society.service;

import com.project.society.model.Notice;
import com.project.society.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeRepository repo;

    public Notice createNotice(Notice notice){
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());
        return repo.save(notice);
    }

    public List<Notice> getNotices(String societyId, List<String> roles){
        return repo.findBySocietyIdAndTargetRolesIn(societyId, roles);
    }

    public Notice updateNotice(String id, Notice updated){
        Notice n = repo.findById(id).orElseThrow(() -> new RuntimeException("Notice not found"));
        n.setTitle(updated.getTitle());
        n.setDescription(updated.getDescription());
        n.setTargetRoles(updated.getTargetRoles());
        n.setUpdatedAt(LocalDateTime.now());
        return repo.save(n);
    }

    public void deleteNotice(String id){ repo.deleteById(id); }
}
