package com.mars.statement.api.group.service;


import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public Group findGroupById(Long id){
        return groupRepository.findById(id).orElse(null);
    }
}
