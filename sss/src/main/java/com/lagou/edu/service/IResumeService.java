package com.lagou.edu.service;

import com.lagou.edu.pojo.Resume;

import java.util.List;

public interface IResumeService {

    public List<Resume> findAll();

    public void deleteById(Long id);

    public Resume save(Resume resume);

    public Resume findById(Long id);
}
