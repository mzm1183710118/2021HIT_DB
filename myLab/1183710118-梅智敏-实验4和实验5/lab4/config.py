disk_dir = './disk/relation/'  # 模拟磁盘所在的目录
tuple_num, blk_num1, blk_num2 = 7, 16, 32  # 每个磁盘块可以保存的元组数目，关系R的磁盘块数，关系S的磁盘块数
nlj_dir, blk_num = './disk/join/nlj/', 8  # 关系连接磁盘目录，缓冲区大小
select_linear, select_binary = './disk/select/linear/', './disk/select/binary/'
BPlusTree_node = './disk/select/BPlusTree/nodes/'
BPlusTree_result = './disk/select/BPlusTree/res/'
project_dir = './disk/project/'  # 投影结果所在的磁盘目录
sort_temp_dir, sort_res_dir = './disk/join/sort/sort/', './disk/join/sort/res/'
hash_temp_dir, hash_res_dir = './disk/join/hash/hash/', './disk/join/hash/res/'
