package weatherwhere.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weatherwhere.team.repository.WeatherRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

}
