package com.lagou.edu.controller;

import com.lagou.edu.pojo.Resume;
import com.lagou.edu.service.IResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private IResumeService resumeService;

    @RequestMapping("/list")
    public ModelAndView getResumeList(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        List<Resume> resumes = resumeService.findAll();
        modelAndView.addObject("resumes", resumes);
        modelAndView.setViewName("resumeList");
        return modelAndView;
    }

    @RequestMapping("/delete")
    public ModelAndView deleteResume( Long id ) {
        resumeService.deleteById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/resume/list");
        return modelAndView;
    }

    @RequestMapping("/save")
    public ModelAndView saveResume(Resume resume) {
        resumeService.save(resume);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/resume/list");
        return modelAndView;
    }

    @RequestMapping("/add")
    public ModelAndView addResume() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add");
        return modelAndView;
    }

    @RequestMapping("/update")
    public ModelAndView updateResume(Long id) {
        Resume resume = resumeService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("resume", resume);
        modelAndView.setViewName("update");
        return modelAndView;
    }

}
