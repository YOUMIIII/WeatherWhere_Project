package weatherwhere.team.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.UrlResource;
import weatherwhere.team.exception.CsvReadingFailedException;
import weatherwhere.team.exception.WeatherApiConnFailedException;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id; // 지역 순번

    @Column
    private String parentRegion; // 시, 도

    @Column
    private String childRegion; // 시, 군, 구

    private Integer nx; // x좌표

    private Integer ny; // y좌표

    @OneToMany(mappedBy = "region")
    private List<Weather> weathers = new ArrayList<>(); // 지역 날씨 정보

    //생성 메서드
    public static List<Region> createOnlyRegion(String resourceLocation) {
        List<Region> regions=new ArrayList<>();
        String fileLocation = resourceLocation + "/init/xypoint.csv";
        Path path = Paths.get(fileLocation);
        URI uri = path.toUri();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                new UrlResource(uri).getInputStream()));

            String line = br.readLine(); // head 떼기
            while ((line = br.readLine()) != null) {
//                log.info("line : {}", line);
                String[] splits = line.split(",");
                if(splits[3].isEmpty()){
                    Region region=new Region();
                    region.setParentRegion(splits[1]);//시,도
                    region.setChildRegion(splits[2]);//시,군,구
//                region.setChildRegion2(splits[3]);//동
                    region.setNx(Integer.parseInt(splits[4]));//Nx
                    region.setNy(Integer.parseInt(splits[5]));//Ny
                    regions.add(region);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CsvReadingFailedException();//unchecked exception
        }
        return regions;
    }

}
