package com.tecsoft.vcommute.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tecsoft.vcommute.dto.DocumentPriceDto;
import com.tecsoft.vcommute.dto.TransportModeDto;
import com.tecsoft.vcommute.model.Mode;

public interface ModeRepository extends JpaRepository<Mode, Integer> {

    boolean existsByLocationIdAndLevelId(Long locationId, Long levelId);

    Optional<Mode> findByLevelIdAndLocationId(Long levelId, Long locationId);

    List<Mode> findAllByEnabled(Boolean enabled);

    @Query(value = "SELECT * FROM mst_mode WHERE grade_id=?1 and location_id=?2", nativeQuery = true)
    List<Mode> fetchModeDataPlainByParameter(@Param("grade_id") Integer grade_id,
            @Param("location_id") Integer location_id);

    @Query(value = "SELECT * FROM mst_mode WHERE grade_id=?1 and location_id=?2", nativeQuery = true)
    List<Mode> fetchModeDataHillByParameter(@Param("grade_id") Integer grade_id,
            @Param("location_id") Integer location_id);

    @Query(value = "select id,base_price,basekm,company_id,enabled,level_id,priceperkm,location_id,grade_id FROM mst_mode WHERE grade_id=?1 and location_id=?2", nativeQuery = true)
    List<Mode> fetchAreaByParameter(@Param("grade_id") Integer grade_id, @Param("location_id") Integer location_id);

    @Query(value = "select a.* FROM mst_mode a left join level_master b on(a.level_id=b.level_id) left join level_des c on(b.lvl_des_id=c.lvl_des_id) where a.grade_id=?1 and des NOT IN(?2) order by id asc", nativeQuery = true)
    List<Mode> fetchAreaByParameterWithoutlocation(@Param("grade_id") Integer grade_id, String levelDes);

    @Query(value = "SELECT a.id FROM mst_mode a left join level_master b on(a.level_id=b.level_id) left join level_des c on(b.lvl_des_id=c.lvl_des_id) where des=?1 and a.grade_id=?2", nativeQuery = true)
    Integer findLevelIdByName(String levelDes, Integer grade_id);

    @Query(value = "select c.des,a.* FROM  mst_mode a left join level_master b on(a.level_id=b.level_id) left join level_des c on(b.lvl_des_id=c.lvl_des_id) where a.id=?1 ", nativeQuery = true)
    Optional<TransportModeDto> fetchModeDes(Integer id);

    @Query(value = "SELECT a.id, c.des, c.document, c.price FROM mst_mode a left join level_master b on(a.level_id = b.level_id) left join level_des c on(b.lvl_des_id = c.lvl_des_id) where a.id= ?1", nativeQuery = true)
    Optional<List<DocumentPriceDto>> fetchRequiredMode(Integer id);
}
