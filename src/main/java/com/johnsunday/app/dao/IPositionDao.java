package com.johnsunday.app.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johnsunday.app.entity.Position;

@Repository
public interface IPositionDao extends JpaRepository<Position, Long> {

	Optional<Position> findByNameIgnoreCase(String positionName);
}
