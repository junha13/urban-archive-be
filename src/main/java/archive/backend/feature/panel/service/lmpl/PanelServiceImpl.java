package archive.backend.feature.panel.service.lmpl;


import archive.backend.feature.panel.service.PanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PanelServiceImpl implements PanelService {

    private final PanelDAO dao;

}
