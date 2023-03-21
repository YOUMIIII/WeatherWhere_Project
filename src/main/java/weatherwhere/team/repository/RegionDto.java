package weatherwhere.team.repository;

import lombok.Data;

@Data
public class RegionDto {

    private Long regionId;

    private String parentRegion;

    private String childRegion;

//    private Weather

}
