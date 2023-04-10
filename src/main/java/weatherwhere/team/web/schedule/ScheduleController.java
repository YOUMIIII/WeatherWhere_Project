package weatherwhere.team.web.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import weatherwhere.team.domain.Schedule;
import weatherwhere.team.domain.member.Member;
import weatherwhere.team.repository.member.MemberRepository;
import weatherwhere.team.service.ScheduleService;
import weatherwhere.team.web.SessionConst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {
    private final MemberRepository memberRepository;
    private final ScheduleService scheduleService;

    @GetMapping("")
    public String schedule(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,Model model){
        model.addAttribute("member", loginMember);

        return "main/schedule";
    }

    @GetMapping("/add")
    public String addSchedule(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,Model model){
        model.addAttribute("member", loginMember);
        model.addAttribute("form",new ScheduleCreateForm());

        return "main/addSchedule";
    }

    @PostMapping("/add")
    public String addSchedule(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @Validated @ModelAttribute("form")ScheduleCreateForm form, BindingResult bindingResult, Model model){
        model.addAttribute("member", loginMember);
        if(form.getStartHour().equals("--")||form.getStartMin().equals("--")||form.getEndHour().equals("--")||form.getEndMin().equals("--")){
            bindingResult.reject("timeCheck","시간 값을 선택하십시오.");
        }
        if(bindingResult.hasErrors()){
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            for(int i=0;i<fieldErrors.size();i++){
//                log.info("fieldErrors.get(i).getField() : {}",fieldErrors.get(i).getField());
//                log.info("fieldErrors.get(i).getRejectedValue() : {}",fieldErrors.get(i).getRejectedValue());
//            }
            Map<String, Object> bindingResultModel = bindingResult.getModel();

//            for (String s : bindingResultModel.keySet()) {
//                log.info("모델맵 key(문자열) : {}",s);
//            }
            for (Object value : bindingResultModel.values()) {
                log.info("value: {} ", value);
            }
            ScheduleCreateForm formTemp = (ScheduleCreateForm) model.getAttribute("form");
            log.info("dateValue {} ", formTemp.getDate());
            model.addAttribute("dateValue",formTemp.getDate());
            model.addAttribute("loc1Value",formTemp.getUserLocation());
            model.addAttribute("loc2Value",formTemp.getUserLocation2());
            model.addAttribute("startHourValue",formTemp.getStartHour());
            model.addAttribute("startMinValue",formTemp.getStartMin());
            model.addAttribute("endHourValue",formTemp.getEndHour());
            model.addAttribute("endMinValue",formTemp.getEndMin());
            return "main/addSchedule";
        }
        Long scheduleId=scheduleService.saveSchedule(Schedule.createSchedule(form,loginMember));
        Schedule savedSchedule = scheduleService.findSchedule(scheduleId);
        log.info("저장됨. 스케줄 제목 : {}",savedSchedule.getTitle());
        return "redirect:/schedule";
    };

    @GetMapping("/edit/{id}")
    public String editScheduleView(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,Model model,@PathVariable("id") Long scheduleId){
        Schedule schedule = scheduleService.findSchedule(scheduleId);
        ScheduleEditForm scheduleEditForm = ScheduleEditForm.createScheduleEditForm(schedule);
        model.addAttribute("member", loginMember);
        model.addAttribute("form",scheduleEditForm);
        return "main/editSchedule";
    }

    @PostMapping("/edit/{id}")
    public String editSchedule(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,@PathVariable("id") Long scheduleId,@Validated @ModelAttribute("form")ScheduleEditForm form, BindingResult bindingResult,Model model){
        model.addAttribute("member", loginMember);
        if(form.getStartHour().equals("--")||form.getStartMin().equals("--")||form.getEndHour().equals("--")||form.getEndMin().equals("--")){
            bindingResult.reject("timeCheck","시간 값을 선택하십시오.");
            return "main/editSchedule";
        }
        if(bindingResult.hasErrors()){
            ScheduleCreateForm formTemp = (ScheduleCreateForm) model.getAttribute("form");
            model.addAttribute("dateValue",formTemp.getDate());
            model.addAttribute("loc1Value",formTemp.getUserLocation());
            model.addAttribute("loc2Value",formTemp.getUserLocation2());
            model.addAttribute("startHourValue",formTemp.getStartHour());
            model.addAttribute("startMinValue",formTemp.getStartMin());
            model.addAttribute("endHourValue",formTemp.getEndHour());
            model.addAttribute("endMinValue",formTemp.getEndMin());
            return "main/editSchedule";
        }
        Schedule target = scheduleService.findSchedule(scheduleId);
        Schedule.editSchedule(target,form);//스케줄 객체값을 세팅
        Long editedScheduleId = scheduleService.saveSchedule(target);//변경
        log.info("저장됨. 스케줄 아이디 : {}",editedScheduleId);
        return "redirect:/schedule";
    };

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                 @PathVariable("id") Long id){
        scheduleService.deleteSchedule(id);
        return "redirect:/schedule";
    }
}
