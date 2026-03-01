package archive.backend.domain.panel.service.lmpl;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface PanelDAO {

    Map<String, Object> login(String a);
}
