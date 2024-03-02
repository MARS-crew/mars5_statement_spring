package com.mars.statement.api.group.service;

import com.mars.statement.api.group.dto.GroupSuggestsDto;

import java.sql.Timestamp;
import java.util.Comparator;

public class SuggestComparator implements Comparator<GroupSuggestsDto> {

    @Override
    public int compare(GroupSuggestsDto o1, GroupSuggestsDto o2) {
        return Timestamp.valueOf(o1.getRegDt().toLocalDateTime())
                .compareTo(o2.getRegDt());
    }
}
