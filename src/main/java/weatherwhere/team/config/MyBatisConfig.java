package weatherwhere.team.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import weatherwhere.team.repository.closet.ClothMapper;
import weatherwhere.team.repository.closet.ClothRepository;
import weatherwhere.team.service.ClothService;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {
    //@Autowired
    private final ClothMapper clothMapper;

    public ClothService clothService() {
        return new ClothService(clothRepository());
    }

    public ClothRepository clothRepository(){
        return new ClothRepository(clothMapper);
    }

}
