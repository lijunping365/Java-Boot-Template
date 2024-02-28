package com.openbytecode.boot.common.tree;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2020-11-06 17:30
 */
public interface ITree<T extends TreeNode<T>> {

  List<T> buildTree(List<T> treeNodes);

  List<T> getChildren(T root, List<T> t);
}
