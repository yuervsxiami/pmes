package com.cnuip.pmes2.service;

import com.cnuip.pmes2.controller.api.request.SpecialistInfo;
import com.cnuip.pmes2.controller.api.request.SpecialistRequest;
import com.cnuip.pmes2.domain.core.College;
import com.cnuip.pmes2.domain.core.Title;

import java.util.List;
import java.util.Map;

public interface SpecialistService {
    void insert(Long userId, SpecialistInfo specialistInfo);

    void update(Long userId, SpecialistInfo specialistInfo);

    SpecialistInfo findOne(Long id);

    Map<String, List<College>> colleges();

    List<Title> titles();

    Map<String, Object> specialistPage(SpecialistRequest specialistRequest);
}
