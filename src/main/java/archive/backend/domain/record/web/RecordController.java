package archive.backend.domain.record.web;

import archive.backend.domain.record.service.RecordService;
import archive.backend.domain.record.service.dto.RecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequestMapping("/api/record")
@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @RequestMapping("/insert")
    public ResponseEntity<?> insertRecord(@RequestPart("record") RecordRequest record, @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(Map.of("result", recordService.insertRecord(record, file)));
    }

    @RequestMapping(value = "/list")
    public ResponseEntity<?> selectRecordList() { // 추후 페이지 받기
        return ResponseEntity.ok(Map.of("result", recordService.selectRecordList()));
    }

    @RequestMapping("/{recordNumber}")
    public ResponseEntity<?> selectRecordDetail(@PathVariable("recordNumber") Long recordNumber) {
        return ResponseEntity.ok(Map.of("result", recordService.selectRecordDetail(recordNumber)));
    }

    @RequestMapping("/update/{recordNumber}")
    public ResponseEntity<?> updateRecord(
            @PathVariable("recordNumber") Long recordNumber,
            @RequestPart("record") RecordRequest record,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(Map.of("result", recordService.updateRecord(recordNumber, record, file)));
    }

    @RequestMapping("/delete/{recordNumber}")
    public ResponseEntity<?> deleteRecord(@PathVariable("recordNumber") Long recordNumber) {
        return ResponseEntity.ok(Map.of("result", recordService.deleteRecord(recordNumber)));
    }
}
