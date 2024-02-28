package com.openbytecode.boot.common.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2020-11-04 15:06
 */
@Data
public class TreeNode<T> implements Serializable {

  private static final long serialVersionUID = 8327326233316757727L;

  private Long id;

  private Long pid;

  private Integer sort;

  private boolean skip;

  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  private List<T> children;
}
