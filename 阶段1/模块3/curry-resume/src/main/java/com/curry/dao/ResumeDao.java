package com.curry.dao;

import com.curry.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 简历数据持久层接口
 *
 * @author curry
 */
public interface ResumeDao extends CrudRepository<Resume, Long>, JpaRepository<Resume, Long> {
}
