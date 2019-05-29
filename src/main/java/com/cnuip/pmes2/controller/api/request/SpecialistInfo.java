package com.cnuip.pmes2.controller.api.request;

import com.cnuip.pmes2.domain.core.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpecialistInfo {
    private Specialist specialist;
    private SpecialistExt specialistExt;
    private List<SpecialistPaper> specialistPapers;
    private List<SpecialistKeyword> specialistKeywords;
    private List<SpecialistIpc> specialistIpcs;
    private List<SpecialistNic> specialistNics;
    private List<SpecialistNtcc> specialistNtccs;
    private List<SpecialistTitle> specialistTitles;
}
