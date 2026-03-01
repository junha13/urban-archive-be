package archive.backend.domain.panel.service.lmpl;


import archive.backend.domain.panel.service.PanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PanelServiceImpl implements PanelService {

    private final PanelDAO dao;

}
