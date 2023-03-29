package weatherwhere.team.repository.region;

import lombok.Data;
import weatherwhere.team.domain.Region;
import weatherwhere.team.service.RegionService;

@Data
public class RegionNameDto {

    private String parentRegion;

    private String childRegion;

    public RegionNameDto(Region region){
        parentRegion=region.getParentRegion();
        childRegion=region.getChildRegion();
    }


}
