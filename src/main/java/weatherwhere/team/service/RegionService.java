package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weatherwhere.team.domain.Region;
import weatherwhere.team.repository.region.RegionJdbcRepository;
import weatherwhere.team.repository.region.RegionRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionJdbcRepository regionJdbcRepository;

    @Value("${resources.location}")
    private String resourceLocation;

    @Value("${weatherApi.serviceKey}")
    private String serviceKey;

    public void post() {
        List<Region> regions = Region.createOnlyRegion(resourceLocation);
        regionJdbcRepository.saveAll(regions);
    }

    public Region updateRegionWeather(Long regionId){
        Region region = regionRepository.findOne(regionId);

        return region;
    }

    public Region findSpecificRegion(String parentRegion,String childRegion,Integer nx,Integer ny){
        return regionRepository.findByNamesAndCo(parentRegion, childRegion, nx, ny);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sampleData(){
        List<Region> regions = Region.createOnlyRegion(resourceLocation);
        regionJdbcRepository.saveAll(regions);

    }

    public List<Region> getRegionList(){
        return regionRepository.findAll();
    }

}
