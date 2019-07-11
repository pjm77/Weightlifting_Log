package pl.pjm77.weightliftinglog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjm77.weightliftinglog.models.Workout;

import javax.transaction.Transactional;

@Transactional
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

}