package weatherwhere.team.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="weather")
public class Weather {

    @Id @GeneratedValue
    @Column(name="weather_id")
    private Long id;

    private String time;
    private Integer temp; // 온도
    private String sky; // 하늘 상태
    private String pop; // 강수확률


    @JoinColumn(name = "region_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    public static Weather createWeather(String time,Integer temp,String sky,String pop){
        Weather weather=new Weather();
        weather.setTime(time);
        weather.setTemp(temp);
        weather.setSky(sky);
        weather.setPop(pop);

        return weather;
    }
    //createWeatherList
    // 여기에서는 객체 1개만 만든다. 그리고
    //생성 메서드를 Region 에서 작성한다
    // 그렇다면 update 도 Region 에서 해야한다
    // 이러면 lastUpdateTime 을 Region 에서 만드는게 낫다. 단 1 개만 생성될 것이기 때문 
    // 10개 제한도 Region 쪽에서 반복문을 10 번만 돌면 된다.
    // -> 시간대 체크 , URL 등도 Region 에서 생성하나? -> RegionService 에서 하자. 그리고 Region 에서는 Weather ... weathers 를 받아서 add 를 반복하자.

    //문제점 : 예제코드의 OrderItem 과 달리, Weather 는 사용자가 선택하여 삭제하는 게 아니다. 시간에 따라 사라지고 생긴다. 이에 맞는 컬렉션은 무엇일까?

    //또한 나중에 API 요청 시간 제한을 걸어야 한다면, 이것또한 '지역' 요청에 대해 걸어야 할 것이다. 만약 전체 날씨테이블에 대해 건다면 요청도 안한 지역에서 제한이 걸리겠지?

}
