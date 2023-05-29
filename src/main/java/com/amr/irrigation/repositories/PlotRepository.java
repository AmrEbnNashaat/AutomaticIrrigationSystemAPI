package com.amr.irrigation.repositories;

import com.amr.irrigation.models.Plot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlotRepository extends JpaRepository<Plot, Integer> {


}
