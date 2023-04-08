package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weatherwhere.team.domain.Region;
import weatherwhere.team.domain.Weather;
import weatherwhere.team.exception.WeatherApiConnFailedException;
import weatherwhere.team.repository.region.RegionRepository;
import weatherwhere.team.repository.weather.WeatherRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final WeatherRepository weatherRepository;

    private final RegionRepository regionRepository;

    @Value("${weatherApi.serviceKey}")
    private String serviceKey;

    public String createWeatherList(String parentRegion,String childRegion){
        //Request URL 에 필요한 값을 생성
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");
        LocalDateTime now = LocalDateTime.now();
        String todayBase = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String yesterdayBase = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));//만약 1AM에 요청한다면 전날 2300을 baseTime 으로 함
        int hour = now.getHour();
        String[] timeList={"2300","0200","0500","0800","1100","1400","1700","2000","2300"};
        int index=((hour+1)*(100))/300;
        String baseDate=(index==0? yesterdayBase:todayBase);
        String baseTime = timeList[index];
        String baseDateTime = baseDate.concat(baseTime);
        log.info("현재 요청 - 기상청 open api basetime : {}", baseTime);
        Long regionId = regionRepository.findByParentChild(parentRegion, childRegion);
        Region region = regionRepository.findOne(regionId);
        String nx = Integer.toString(region.getNx());
        String ny = Integer.toString(region.getNy());

        //이미 존재하는 데이터가 있는지 체크
        boolean baseTimeCheck = region.getWeathers().stream()
                .anyMatch(weather -> weather.getBaseDateTime().equals(baseDateTime));
        if(baseTimeCheck){
            log.info("동일 지역 동일 시간대 요청 있음");
            return baseDateTime;
        }
        //좌표는 같지만 이름이 다른 지역의 데이터를 체크 (요청횟수줄이기)
        List<Weather> existingData = weatherRepository.findByNxNyBaseTime(region.getNx(), region.getNy(), baseDateTime);
//        List<Weather> copyList = new ArrayList<>();
        if(!existingData.isEmpty()){
            for (Weather existingDatum : existingData) {
                Weather copy=Weather.createWeather(
                        existingDatum.getBaseDateTime(),
                        existingDatum.getWeatherDateTime(),
                        existingDatum.getTemp(),
                        existingDatum.getSky(),
                        existingDatum.getPty(),
                        existingDatum.getPop(),
                        existingDatum.getNx(),
                        existingDatum.getNy());
                copy.setRegion(region);
            }
//            region.setWeathers(copyList);
            log.info("동일 시간대의 타지역 요청 있음");
            return baseDateTime;
        }

        //존재하는 데이터가 없으면 OPEN API에 요청
        HttpURLConnection conn= null;
        StringBuilder resultBuilder= null;
        try {
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8")+ "=" +  serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("290", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/
            URL url = new URL(urlBuilder.toString());

            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type","application/json");

            BufferedReader bufferedReader;
            if(conn.getResponseCode()>=200 && conn.getResponseCode()<=300){
                bufferedReader =new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }else{
                bufferedReader =new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            resultBuilder = new StringBuilder();
            String line;
            while((line= bufferedReader.readLine())!=null){
                resultBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new WeatherApiConnFailedException("날씨 API 연동 실패");
        }

        conn.disconnect();
        String data=resultBuilder.toString();//응답 수신 완료
        log.info("기상청 API로부터 날씨값 가져옴(같은 지역을 1시간 이내로 요청시 이 로그가 보이면 안 됩니다)");
        /*-------- JSON 파싱 --------*/
        String date=null;
        String time=null;
        String temp=null;
        String sky=null;
        String pty=null;//강수형태
        String pop=null;//강수확률

        JSONObject jObject = new JSONObject(data);
        JSONObject response = jObject.getJSONObject("response");
        JSONObject body = response.getJSONObject("body");
        JSONObject items = body.getJSONObject("items");
        JSONArray jArray = items.getJSONArray("item");

//        List<Weather> weatherList = new ArrayList<>();
        int count=0;
        for(int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            String fcstDate = obj.getString("fcstDate");
            String fcstTime=obj.getString("fcstTime");//날씨 시간

            if(i==0){//NPE방지
                time=fcstTime;
            }

            if(!time.equals(fcstTime)){//fcstTime 이 변했을 경우
                String weatherDateTime = date.concat(time);// fcstDate , fcstTime (X) date , time (O)
                LocalDateTime compareDateTime = LocalDateTime.parse(weatherDateTime, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
                //break 조건 B : 현재 시간 기준 (적용중, 서버 오래 지속되면 DTO 에서 반복문 돌면서 지난 시간 제거)
                if(now.plusHours(24).isBefore(compareDateTime)){
                    break;
                }
                //시간 필터 조건 A : 요청 시간 기준
//                if(now.minusHours(1).isBefore(compareDateTime)){
                    Weather weather = Weather.createWeather(
                            baseDateTime,
                            weatherDateTime,
                            Integer.parseInt(temp),
                            Integer.parseInt(sky),
                            Integer.parseInt(pty),
                            pop==null? 0:Integer.parseInt(pop),
                            Integer.parseInt(nx),
                            Integer.parseInt(ny));
                    weatherRepository.save(weather);
                    weather.setRegion(region);
                    count++;
                    //weatherList.add(weather);
                    log.info("요청 base time : {}",weather.getBaseDateTime());
                    log.info("지역 날씨 time : {}",weather.getWeatherDateTime());
                    log.info("지역 날씨 tmp : {}", weather.getTemp());
                    log.info("지역 날씨 sky : {}", weather.getSky());
                    log.info("지역 날씨 pop : {}", weather.getPop());
//                }else{
//                    log.info("현재시간 {} , fcstTime {} 은 건너뛰고 저장합니다(지난 시간)",now, compareDateTime);
//                }

            }

            //break 조건 A : 요청 시간 기준
//            if(count==24){
//                break;
//            }

            String category = obj.getString("category");
            String fcstValue = (String)obj.get("fcstValue");

            switch (category) {
                case "TMP":
                    temp = fcstValue;
                    break;
                case "SKY":
                    sky = fcstValue;
                    break;
                case "PTY":
                    pty = fcstValue;
                    break;
                case "POP":
                    pop = fcstValue;
                    break;
            }
            date=fcstDate;
            time=fcstTime;
        }
        return baseDateTime;
    }

    public String createPastWeatherList(String parentRegion,String childRegion,String requestDate){
        //Request URL 에 필요한 값을 생성
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");
        String baseDate=requestDate.replaceAll("\\D","");
        String baseTime="0500";
        String baseDateTime = baseDate.concat(baseTime);
        log.info("과거 요청 - 기상청 open api baseDate : {}",baseDate );
        Long regionId = regionRepository.findByParentChild(parentRegion, childRegion);
        Region region = regionRepository.findOne(regionId);
        String nx = Integer.toString(region.getNx());
        String ny = Integer.toString(region.getNy());

        //이미 존재하는 데이터가 있는지 체크
        boolean baseTimeCheck = region.getWeathers().stream()
                .anyMatch(weather -> weather.getBaseDateTime().equals(baseDateTime));
        if (baseTimeCheck) {
            log.info("동일 지역 동일 시간대 요청 있음");

            return baseDateTime;
        }
        //좌표는 같지만 이름이 다른 지역의 데이터를 체크 (요청횟수 줄이기) - 있으면 날씨값을 복사
        List<Weather> existingData = weatherRepository.findByNxNyBaseTime(region.getNx(), region.getNy(), baseDateTime);
        ;
        if (!existingData.isEmpty()) {
            for (Weather existingDatum : existingData) {
                Weather copy = Weather.createWeather(
                        existingDatum.getBaseDateTime(),
                        existingDatum.getWeatherDateTime(),
                        existingDatum.getTemp(),
                        existingDatum.getSky(),
                        existingDatum.getPty(),
                        existingDatum.getPop(),
                        existingDatum.getNx(),
                        existingDatum.getNy());
                copy.setRegion(region);
            }
            log.info("동일 시간대의 타지역 요청 있음");

            return baseDateTime;
        }

        //존재하는 데이터가 없으면 OPEN API에 요청
        HttpURLConnection conn = null;
        StringBuilder resultBuilder = null;
        try {
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("336", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/
            URL url = new URL(urlBuilder.toString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader bufferedReader;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            resultBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new WeatherApiConnFailedException("날씨 API 연동 실패");
        }

        conn.disconnect();
        String data = resultBuilder.toString();//응답 수신 완료
        log.info("기상청 API로부터 날씨값 가져옴(같은 지역을 1시간 이내로 요청시 이 로그가 보이면 안 됩니다)");


        /*-------- JSON 파싱 --------*/


        JSONObject jObject = new JSONObject(data);
        JSONObject response = jObject.getJSONObject("response");
        //TODO 예외 처리 또는 html 달력 날짜 제한 필요
        //시간이 지나 데이터가 사라졌을 경우  org.json.JSONException: JSONObject["body"] not found. 에러 발생함
        JSONObject body = response.getJSONObject("body");
        JSONObject items = body.getJSONObject("items");
        JSONArray jArray = items.getJSONArray("item");


        int count = 0;

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            String fcstDate = obj.getString("fcstDate");
            String fcstTime = obj.getString("fcstTime");//날씨 시간
            String category = obj.getString("category");
            String fcstValue = (String) obj.get("fcstValue");

            if (category.equals("TMX") || category.equals("TMN")) {
                Weather weather = Weather.createWeather(
                        baseDateTime,
                        fcstDate.concat(fcstTime),
                        (int)Double.parseDouble(fcstValue),
                        0,
                        0,
                        0,
                        Integer.parseInt(nx),
                        Integer.parseInt(ny));
                weatherRepository.save(weather);
                weather.setRegion(region);
                count++;
            }

            if (count == 2) {
                log.info("과거 날씨 생성 완료 (기온만)");
                break;
            }
        }

        return baseDateTime;
    }

}
