package univ.yesummit.domain.summit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.summit.entity.Summit;

public interface SummitRepository extends JpaRepository<Summit, Long> {
}
