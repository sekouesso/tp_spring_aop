package com.x.sentinel.repositories;

import com.x.sentinel.entities.FeeComputation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeeComputationRepository extends JpaRepository<FeeComputation, UUID> {

}
