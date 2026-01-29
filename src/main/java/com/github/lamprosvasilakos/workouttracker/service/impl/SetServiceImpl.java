package com.github.lamprosvasilakos.workouttracker.service.impl;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateSetRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateSetRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.SetResponse;
import com.github.lamprosvasilakos.workouttracker.entity.Set;
import com.github.lamprosvasilakos.workouttracker.entity.WorkoutExercise;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;
import com.github.lamprosvasilakos.workouttracker.mapper.SetMapper;
import com.github.lamprosvasilakos.workouttracker.repository.SetRepository;
import com.github.lamprosvasilakos.workouttracker.service.SetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SetServiceImpl implements SetService {

    private final SetRepository setRepository;
    private final SetMapper setMapper;

    @Override
    @Transactional
    public SetResponse createSet(CreateSetRequest request, WorkoutExercise workoutExercise) {
        Set set = setMapper.toEntity(request);
        set.setWorkoutExercise(workoutExercise);
        Set savedSet = setRepository.save(set);
        log.info("Created new set for workout exercise {}", workoutExercise.getId());
        return setMapper.toResponse(savedSet);
    }

    @Override
    @Transactional
    public SetResponse updateSet(UUID setId, UpdateSetRequest request) throws AppObjectNotFoundException {
        Set set = findSetById(setId);
        setMapper.updateEntityFromRequest(request, set);
        Set updatedSet = setRepository.save(set);
        log.info("Updated set with ID {}", setId);
        return setMapper.toResponse(updatedSet);
    }

    @Override
    @Transactional
    public void deleteSet(UUID setId) throws AppObjectNotFoundException {
        Set set = findSetById(setId);
        setRepository.delete(set);
        log.info("Deleted set with ID {}", setId);
    }

    @Override
    @Transactional(readOnly = true)
    public SetResponse getSetById(UUID setId) throws AppObjectNotFoundException {
        Set set = findSetById(setId);
        return setMapper.toResponse(set);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SetResponse> getAllSetsByWorkoutExerciseId(UUID workoutExerciseId) {
        return setRepository.findAllByWorkoutExerciseId(workoutExerciseId).stream()
                .map(setMapper::toResponse)
                .collect(Collectors.toList());
    }

    private Set findSetById(UUID setId) throws AppObjectNotFoundException {
        return setRepository.findById(setId)
                .orElseThrow(() -> new AppObjectNotFoundException("Set", "Set with ID " + setId + " not found"));
    }
}
