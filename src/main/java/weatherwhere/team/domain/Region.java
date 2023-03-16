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

    @Column(name = "region_upper")
    private String parentRegion; // 시, 도

    @Column(name = "region_lower")
    private String childRegion; // 시, 군, 구

    @Column(name = "region_lower2")
    private String childRegion2; // 동

    private int nx; // x좌표

    private int ny; // y좌표

    @OneToMany(mappedBy = "region",cascade=CascadeType.ALL)
    private List<Weather> weathers = new ArrayList<>(); // 지역 날씨 정보

    private String lastUpdateTime; // 마지막 갱신 시각 (시간 단위)

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
                Region region=new Region();
                region.setParentRegion(splits[1]);//시,도
                region.setChildRegion(splits[2]);//시,군,구
                region.setChildRegion2(splits[3]);//동
                region.setNx(Integer.parseInt(splits[4]));//Nx
                region.setNy(Integer.parseInt(splits[5]));//Ny
                regions.add(region);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CsvReadingFailedException();//unchecked exception
        }
        return regions;
    }

    public void addWeather(Weather weather){
        weathers.add(weather);
        weather.setRegion(this);
    }

    public void createRegionWeathers(String serviceKey){

        StringBuilder urlBuilder=new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");

        LocalDateTime now = LocalDateTime.now();
        String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int hour = now.getHour();
        int min = now.getMinute();
        String[] timeList={"2310","0210","0510","0810","1110","1410","1710","2010","2310"};
        int index=((hour+1)*(100)+min-10)/300;
        String hourStr = timeList[index];
        String nx = Integer.toString(getNx());
        String ny = Integer.toString(getNy());
        String currentChangeTime = now.format(DateTimeFormatter.ofPattern("yy.MM.dd ")) + hour;

        if(weathers!=null&&lastUpdateTime!=null){
            if(lastUpdateTime.equals(currentChangeTime)){
                log.info("기존 자료를 재사용합니다");
                return;
            }
        }
        HttpURLConnection conn= null;
        StringBuilder sb= null;

        try {
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8")+ "=" +  serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8")); /*‘21년 6월 28일 발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(hourStr, "UTF-8")); /*06시 발표(정시단위) */
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/

            URL url = new URL(urlBuilder.toString());



            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type","application/json");

            BufferedReader rd;
            if(conn.getResponseCode()>=200 && conn.getResponseCode()<=300){
                rd=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }else{
                rd=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while((line=rd.readLine())!=null){
                sb.append(line);
            }
            rd.close();
        } catch (IOException e) {
            throw new WeatherApiConnFailedException("날씨 API 연동 실패");
        }

        conn.disconnect();
        String data=sb.toString();//응답 수신 완료

        /*-------- JSON 파싱 --------*/
        String time=null;
        String temp=null;
        String sky=null;
        String pop=null;

        JSONObject jObject = new JSONObject(data);
        JSONObject response = jObject.getJSONObject("response");
        JSONObject body = response.getJSONObject("body");
        JSONObject items = body.getJSONObject("items");
        JSONArray jArray = items.getJSONArray("item");

        //반복문 탈출용 (16시간후까지만 받음)
        LocalTime hourRange = LocalTime.now().withMinute(00).plusHours(17);
        String hourRangeStr = hourRange.format(DateTimeFormatter.ofPattern("HHmm"));

        for(int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);


            String fcstTime=obj.getString("fcstTime");//날씨 시간
            
            if(i==0){//NPE방지
                time=fcstTime;
            }
            if(fcstTime.equals(hourRangeStr)){
                log.info("fcstTime : {} 시까지만 값을 받아옵니다.", fcstTime);
                break;
            }

            if(!time.equals(fcstTime)){
                Weather weather=Weather.createWeather(time,Integer.parseInt(temp),sky,pop);
                addWeather(weather);
            }

            String category = obj.getString("category");
            String fcstValue = (String)obj.get("fcstValue");

            switch (category) {
                case "TMP":
                    temp = fcstValue;
                    break;
                case "SKY":
                    sky = fcstValue;
                    break;
                case "POP":
                    pop = fcstValue;
                    break;
            }
            time=fcstTime;

        }//JSON 받아오기 끝

        lastUpdateTime=currentChangeTime;
        log.info("기상청 API로부터 날씨값 가져옴(같은 지역을 1시간 이내로 요청시 이 로그가 보이면 안 됩니다)");
        for (Weather weather : weathers) {
            log.info("지역 날씨 time : {}",weather.getTime());
            log.info("지역 날씨 tmp : {}", weather.getTemp());
            log.info("지역 날씨 sky : {}", weather.getSky());
            log.info("지역 날씨 pop : {}", weather.getPop());
        }
    }


}
