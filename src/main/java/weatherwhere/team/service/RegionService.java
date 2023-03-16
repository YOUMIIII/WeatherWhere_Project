package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weatherwhere.team.domain.Region;
import weatherwhere.team.repository.RegionJdbcRepository;
import weatherwhere.team.repository.RegionRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
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
        region.createRegionWeathers(serviceKey);
        return region;
    }



}
