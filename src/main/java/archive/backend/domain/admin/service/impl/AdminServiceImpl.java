package archive.backend.domain.admin.service.impl;

import archive.backend.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminDAO adminDAO;

}
