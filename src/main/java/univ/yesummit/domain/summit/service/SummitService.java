package univ.yesummit.domain.summit.service;

import univ.yesummit.domain.summit.dto.SummitResponseDTO;

import java.util.List;

public interface SummitService {

    List<SummitResponseDTO> getAllSummits();

    SummitResponseDTO getSummitById(Long id);
}
