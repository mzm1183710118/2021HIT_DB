#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
查询优化算法的设计与实现.
"""
from PyQt5.QtWidgets import QApplication, QMainWindow, QTableWidgetItem, QHeaderView, QTreeWidgetItem, QStyleFactory
from gui import Ui_MainWindow
import sys

query0 = 'SELECT [ ENAME = ’Mary’ & DNAME = ’Research’ ] ( EMPLOYEE JOIN DEPARTMENT )'
query1 = 'SELECT [ ESSN = ’01’ ] ( PROJECTION [ ESSN, PNAME ] ( WORKS_ON JOIN PROJECT ) )'
query2 = 'PROJECTION [ BDATE ] ( SELECT [ ENAME = ’John’ & DNAME = ’ Research’ ] ( EMPLOYEE JOIN DEPARTMENT ) )'
queries = [query0, query1, query2]


class TreeNode:
    def __init__(self, op='', info=''):
        self.child = []  # 儿子节点
        self.op = op  # 此节点代表的操作：Select、project、join
        self.info = info

    def __str__(self):
        return (self.op if self.op else '') + (' ' + self.info if self.info else '')


class MainWindow(QMainWindow):
    def __init__(self, ui_main_win: Ui_MainWindow):
        super().__init__()
        self.ui = ui_main_win
        ui_main_win.setupUi(self)
        self.set_query()

    def set_query(self):
        self.ui.query_table.setRowCount(len(queries))
        self.ui.query_table.horizontalHeader().setSectionResizeMode(0, QHeaderView.ResizeToContents)
        self.ui.query_table.horizontalHeader().setSectionResizeMode(1, QHeaderView.Stretch)
        for idx, query in enumerate(queries):
            self.ui.query_table.setItem(idx, 0, QTableWidgetItem('query%d' % idx))
            self.ui.query_table.setItem(idx, 1, QTableWidgetItem(query))
        self.ui.query_box.addItems(['query%d' % idx for idx in range(len(queries))])

    def query(self):
        root = get_tree(queries[self.ui.query_box.currentIndex()])
        if self.ui.optimize_on.isChecked():
            root = optimize(root)
        # output_tree(root)
        tree_stack, item_stack = [root], [QTreeWidgetItem(self.ui.parse_tree)]
        while tree_stack:
            tree_node, item_node = tree_stack.pop(0), item_stack.pop(0)
            item_node.setText(0, str(tree_node))
            tree_stack = tree_node.child + tree_stack
            item_stack = [QTreeWidgetItem(item_node) for child in tree_node.child] + item_stack
        self.ui.parse_tree.expandAll()
        self.ui.parse_tree.setStyle(QStyleFactory.create('windows'))
        self.ui.parse_tree.header().setSectionResizeMode(QHeaderView.ResizeToContents)


def get_tree(query: str):
    tokens, idx, node = query.split(), 0, TreeNode()
    # 遍历token
    while idx < len(tokens):
        token = tokens[idx]
        # 这些是operation，即op
        if token == 'SELECT' or token == 'PROJECTION':
            # 找到[]的范围
            end = tokens.index(']', idx)
            node.op, node.info = token, ' '.join(tokens[idx + 2:end])
            idx = end + 1
        elif token == 'JOIN':
            node.op = token
            node.child.append(TreeNode(info=tokens[idx - 1]))  # 连接操作的第一个关系
            node.child.append(TreeNode(info=tokens[idx + 1]))  # 连接操作的第二个关系
            idx += 1
        elif token == '(':  # 括号内为查询子句，子句所在的子树应该更靠近root
            # count用来记录子查询的层数（可能有多个嵌套子查询），idy用来在子查询中遍历tokens
            count, idy = 1, idx + 1
            while idy < len(tokens) and count > 0:
                if tokens[idy] == '(':
                    count += 1
                elif tokens[idy] == ')':
                    count -= 1
                idy += 1
            node.child.append(get_tree(' '.join(tokens[idx + 1:idy - 1])))
            idx = idy
        else:
            # 恐慌模式，忽略不合法的输入序列
            idx += 1
    return node


def output_tree(node: TreeNode, sep=''):
    print(sep + str(node))
    if len(node.child) >= 1:
        output_tree(node.child[0], sep + '\t')
    if len(node.child) >= 2:
        output_tree(node.child[1], sep + '\t')


def optimize(node: TreeNode, info_lst=None) -> TreeNode:
    if node.op == 'SELECT':
        # select下推，用优化后的节点替换自己
        node = optimize(node.child[0], node.info.split('&'))
    elif node.op == 'PROJECTION':
        # 对自己的孩子节点进行优化
        node.child[0] = optimize(node.child[0], info_lst)
    elif node.op == 'JOIN':
        # 依据传递来的信息（来自select语句的info_lst）进行优化各个孩子
        node0 = TreeNode(op='SELECT', info=info_lst[0])
        node0.child.append(node.child[0])
        node.child[0] = node0
        if len(info_lst) > 1:
            node1 = TreeNode(op='SELECT', info=info_lst[1])
            node1.child.append(node.child[1])
            node.child[1] = node1
    return node
