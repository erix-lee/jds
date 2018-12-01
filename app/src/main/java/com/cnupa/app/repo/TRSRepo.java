package com.cnupa.app.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cnupa.app.model.TRS;

public interface TRSRepo extends JpaRepository<TRS, Long> {
	Page<TRS> findByChiefId(Pageable pageable, Long chiefId);
}
