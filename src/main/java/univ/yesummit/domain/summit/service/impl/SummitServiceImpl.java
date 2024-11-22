package univ.yesummit.domain.summit.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import univ.yesummit.domain.summit.dto.SummitResponseDTO;
import univ.yesummit.domain.summit.entity.Summit;
import univ.yesummit.domain.summit.repository.SummitRepository;
import univ.yesummit.domain.summit.service.SummitService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SummitServiceImpl implements SummitService {

    private final SummitRepository summitRepository;

    @Override
    public List<SummitResponseDTO> getAllSummits() {
        List<Summit> summits = summitRepository.findAll();

        // 엔티티 리스트를 DTO 리스트로 변환
        return summits.stream()
                .map(summit -> new SummitResponseDTO(summit.getTitle(), summit.getContent()))
                .collect(Collectors.toList());
    }

    @Override
    public SummitResponseDTO getSummitById(Long id) {
        Summit summit = summitRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("써밋을 찾을 수 없습니다"));

        return new SummitResponseDTO(summit.getTitle(), summit.getContent());
    }
}
