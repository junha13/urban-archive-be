package archive.backend.domain.record.web;

import archive.backend.domain.record.service.RecordService;
import archive.backend.domain.record.service.dto.RecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequestMapping("/api/record")
@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/insert")
    public ResponseEntity<?> insertRecord(Authentication authentication, @RequestPart("record") RecordRequest record, @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(Map.of("result", recordService.insertRecord(authentication.getName(), record, file)));
    }

    @GetMapping("/list")
    public ResponseEntity<?> selectRecordList(@RequestParam(value = "type", required = false) String type) {
        return ResponseEntity.ok(Map.of("result", recordService.selectRecordList(type)));
    }

    @GetMapping("/{recordNumber}")
    public ResponseEntity<?> selectRecordDetail(@PathVariable("recordNumber") Long recordNumber) {
        return ResponseEntity.ok(Map.of("result", recordService.selectRecordDetail(recordNumber)));
    }

    @PutMapping("/update/{recordNumber}")
    public ResponseEntity<?> updateRecord(
            Authentication authentication,
            @PathVariable("recordNumber") Long recordNumber,
            @RequestPart("record") RecordRequest record,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(Map.of("result", recordService.updateRecord(recordNumber, authentication.getName(), hasAdminRole(authentication), record, file)));
    }

    @DeleteMapping("/delete/{recordNumber}")
    public ResponseEntity<?> deleteRecord(Authentication authentication, @PathVariable("recordNumber") Long recordNumber) {
        return ResponseEntity.ok(Map.of("result", recordService.deleteRecord(recordNumber, authentication.getName(), hasAdminRole(authentication))));
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
