package archive.backend.domain.panel.web;

import archive.backend.domain.panel.service.PanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/panel")
@RestController
@RequiredArgsConstructor
public class PanelController {

    private final PanelService service;


}
