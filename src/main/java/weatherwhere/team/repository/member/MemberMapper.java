package weatherwhere.team.repository.member;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import weatherwhere.team.domain.member.Member;

@Mapper
public interface MemberMapper {
    void save(Member member);


    //아이디를 넣으면 멤버값을 다 가져옴
    Member login(@Param("userId")String loginId, @Param("userPw")String loginPw);

}
