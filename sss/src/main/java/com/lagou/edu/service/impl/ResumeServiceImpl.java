package com.lagou.edu.service.impl;

import com.lagou.edu.dao.ResumeDao;
import com.lagou.edu.pojo.Resume;
import com.lagou.edu.service.IResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("resumeService")
@Transactional
public class ResumeServiceImpl implements IResumeService {

    @Autowired
    private ResumeDao resumeDao;

    @Override
    public List<Resume> findAll() {
        return resumeDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        resumeDao.deleteById(id);
    }

    @Override
    public Resume save(Resume resume) {
        return resumeDao.save(resume);
    }

    @Override
    public Resume findById(Long id) {
        Optional<Resume> optional = resumeDao.findById(id);
        return optional.get();
    }
}
