package weatherwhere.team.domain.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Location {
    String code;
    String displayName;

    public Location(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
}
