package weatherwhere.team.repository.weather;

import lombok.Data;

@Data
public class OpenApiDto {

    private String baseDate;
    private String baseTime;
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private Integer nx;
    private Integer ny;
}
