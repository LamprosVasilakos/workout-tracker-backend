package com.github.lamprosvasilakos.workouttracker.mapper;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateSetRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.SetResponse;
import com.github.lamprosvasilakos.workouttracker.entity.Set;
import org.springframework.stereotype.Component;

@Component
public class SetMapper {

    public Set toEntity(CreateSetRequest request) {
        return Set.builder()
                .reps(request.reps())
                .weight(request.weight())
                .notes(request.notes())
                .setType(request.setType())
                .build();
    }

    public SetResponse toResponse(Set set) {
        return new SetResponse(
                set.getId(),
                set.getReps(),
                set.getWeight(),
                set.getNotes(),
                set.getSetType()
        );
    }
}
