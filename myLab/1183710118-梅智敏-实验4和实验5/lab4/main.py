from math import ceil

import extmem
from config import blk_num, tuple_num
from execution.join import nested_loop_join, sort_merge_join, hash_join, get_res
from execution.project import relation_project
from execution.select import linear_search, binary_search, BPlusTree_search

if __name__ == '__main__':
    buffer = extmem.Buffer(blk_num)
    linear_search(buffer)  # 关系选择，线性搜索
    print('关系选择-linearSearch的磁盘IO次数为：%d' % buffer.io_num)
    binary_search(buffer)
    print('关系选择-binarySearch的磁盘IO次数为：%d' % buffer.io_num)
    BPlusTree_search(buffer)
    print('关系选择-BPlusTree_search的磁盘IO次数为：%d' % buffer.io_num)
    relation_project(buffer)  # 关系投影
    print('关系投影的磁盘IO次数为：%d' % buffer.io_num)
    nested_loop_join(buffer)
    print('nest-loop-join算法的磁盘IO次数为：%d' % buffer.io_num)
    sort_merge_join(buffer)
    print('sort-merge-join算法的磁盘IO次数为：%d' % buffer.io_num)
    hash_join(buffer)
    print('hash-join算法的磁盘IO次数为：%d' % buffer.io_num)
    # print('测试的连接块数为：%d' % ceil(len(get_res()) / int(tuple_num / 2)))
