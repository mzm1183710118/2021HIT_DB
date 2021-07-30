import extmem
from config import project_dir, tuple_num
from extmem import Buffer, blk_num1, disk_dir


def relation_project(buffer: Buffer):
    """
    关系投影，对R的A属性进行投影并需要去重，并将结果写入到磁盘中
    :param buffer: 缓冲区
    """
    extmem.drop_blk_in_dir(project_dir)  # 删除文件夹下的所有模拟磁盘文件
    buffer.io_num, res, count, = 0, [], 0  # 投影选择的结果
    all_res = set()
    for disk_idx in range(blk_num1):
        index = buffer.load_blk('%sr%d.blk' % (disk_dir, disk_idx))  # 加载磁盘块内容到缓冲区中
        for data in buffer.data[index]:
            if data.split()[0] not in all_res:  # 去重
                res.append(data.split()[0])
                all_res.add(data.split()[0])
                # 因为投影后只有一个属性A，故一个blk原来可以存储tuple_num个元组，现在可以存储tuple_num*2个
                if len(res) == tuple_num * 2:
                    buffer.write_buffer(res, '%sr%d.blk' % (project_dir, count))
                    res, count = [], count + 1
        buffer.free_blk(index)
    if res:
        buffer.write_buffer(res, '%sr%d.blk' % (project_dir, count))