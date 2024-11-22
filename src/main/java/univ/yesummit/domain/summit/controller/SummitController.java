package univ.yesummit.domain.summit.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import univ.yesummit.domain.summit.dto.SummitResponseDTO;
import univ.yesummit.domain.summit.repository.SummitRepository;
import univ.yesummit.domain.summit.service.SummitService;
import univ.yesummit.global.exception.dto.ResponseVO;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/summit")
public class SummitController {


    private final SummitService summitService;

    @GetMapping
    @Operation(summary = "모든 써밋 가져오기", description = "모든 써밋의 제목과 내용을 리스트로 가져옵니다.")
    public ResponseVO<List<SummitResponseDTO>> getAllSummits() {
        log.info("[+] Fetching all Summit data");

        List<SummitResponseDTO> summits = summitService.getAllSummits();
        return new ResponseVO<>(summits);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID로 써밋 가져오기", description = "특정 ID를 가진 써밋의 제목과 내용을 가져옵니다.")
    public ResponseVO<SummitResponseDTO> getSummitById(@PathVariable Long id) {
        log.info("[+] Fetching Summit data by ID: {}", id);

        SummitResponseDTO summit = summitService.getSummitById(id);
        return new ResponseVO<>(summit);
    }
}
