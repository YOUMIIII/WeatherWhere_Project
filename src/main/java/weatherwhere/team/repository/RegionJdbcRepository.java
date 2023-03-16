package weatherwhere.team.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import weatherwhere.team.domain.Region;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RegionJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    @Value("${batchSize}")
    private int batchSize;//500
    public void saveAll(List<Region> regions){
        int batchCount=0;
        List<Region> regionList = new ArrayList<>();
        for(int i=0;i<regions.size();i++){
            regionList.add(regions.get(i));
            if((i+1)%batchSize==0){//loop 횟수가 batchSize 의 배수이면 insert 쿼리 날림
                batchCount=batchInsert(batchSize,batchCount,regionList);
            }
        }
        if(!regionList.isEmpty()){//남은 값이 있을 경우
            batchCount = batchInsert(batchSize, batchCount, regionList);
        }
        log.info("batchCount : {}", batchCount);//쿼리 날린 횟수?
    }

    private int batchInsert(int batchSize, int batchCount, List<Region> regionList) {
        jdbcTemplate.batchUpdate("INSERT INTO REGION (REGION_UPPER,REGION_LOWER,REGION_LOWER2,NX,NY) VALUES (?,?,?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1,regionList.get(i).getParentRegion());
                ps.setString(2,regionList.get(i).getChildRegion());
                ps.setString(3,regionList.get(i).getChildRegion2());
                ps.setInt(4,regionList.get(i).getNx());
                ps.setInt(5,regionList.get(i).getNy());
            }

            @Override
            public int getBatchSize() {
                return regionList.size();
            }
        });
        regionList.clear();
        batchCount++;
        return batchCount;
    }
}
