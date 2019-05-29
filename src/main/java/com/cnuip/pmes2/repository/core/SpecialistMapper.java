package com.cnuip.pmes2.repository.core;

import com.cnuip.pmes2.domain.core.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistMapper {
    long insert(Specialist specialist);

    void insertExt(SpecialistExt specialistExt);

    void insertTitle(List<SpecialistTitle> list);

    void insertPaper(List<SpecialistPaper> specialistPaper);

    void insertKeyword(List<SpecialistKeyword> specialistKeyword);

    void insertIpc(List<SpecialistIpc> list);

    void insertNic(List<SpecialistNic> list);

    void insertNtcc(List<SpecialistNtcc> list);

    long update(Specialist specialist);

    void updateExt(SpecialistExt specialistExt);

    void deleteTitle(Long expertId);

    void deletePaper(Long expertId);

    void deleteKeyword(Long expertId);

    void deleteIpc(Long expertId);

    void deleteNic(Long expertId);

    void deleteNtcc(Long expertId);

    Specialist findById(Long id);

    SpecialistExt findExtById(Long id);

    List<SpecialistTitle> findTitleById(Long id);

    List<SpecialistKeyword> findKeywordById(Long id);

    List<SpecialistPaper> findPaperById(Long id);

    List<SpecialistIpc> findIpcById(Long id);

    List<SpecialistNic> findNicById(Long id);

    List<SpecialistNtcc> findNtccById(Long id);
}
