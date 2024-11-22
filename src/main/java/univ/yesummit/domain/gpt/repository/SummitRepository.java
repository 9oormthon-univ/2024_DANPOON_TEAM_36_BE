package univ.yesummit.domain.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.gpt.entity.Summit;

public interface SummitRepository extends JpaRepository<Summit, Long> {
}
