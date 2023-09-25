package com.icia.member.controller;

import com.icia.member.dto.MemberDTO;
import com.icia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/save")
    public String save() {
        return "/memberPages/memberSave";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return "/memberPages/memberLogin";
    }

    @GetMapping("/member/login")
    public String loginForm() {
        return "/memberPages/memberLogin";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            session.setAttribute("id",loginResult.getId());
            return "/memberPages/memberMain";
        } else {
            // login 실패
            return "/memberPages/memberLogin";
        }
    }



    @GetMapping("/members")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "/memberPages/memberList";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {

        try {
            MemberDTO memberDTO = memberService.findById(id);
            model.addAttribute("member", memberDTO);
            return "/memberPages/memberDetail";
        } catch (NoSuchElementException e) {
            return "/memberPages/NotFound";
        }
    }

    @GetMapping("/member/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        try {
            MemberDTO memberDTO = memberService.findById(id);
            model.addAttribute("member", memberDTO);
            return "/memberPages/memberUpdate";
        } catch (NoSuchElementException e) {
            return "/memberPages/NotFound";
        }
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("memberDTO = " + memberDTO);
        memberService.update(memberDTO);
        return "redirect:/members";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @PostMapping("/member/dup-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
    }

    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/members";
    }

    @PostMapping("/member/delete/")
    public @ResponseBody List<MemberDTO> deleteByIdUseAxios(@RequestParam("id") Long id) {
        System.out.println("id = " + id);
        memberService.deleteById(id);
        List<MemberDTO> memberDTOList = memberService.findAll();
        return memberDTOList;
    }


}
