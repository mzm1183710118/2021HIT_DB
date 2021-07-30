from copy import deepcopy

import extmem
from config import select_linear, disk_dir, tuple_num, select_binary, sort_temp_dir, BPlusTree_node, \
    BPlusTree_result
from execution.join import external_sort
import re


def linear_search(buffer: extmem.Buffer):
    """
    关系选择：线性搜索R.A=40, S.C=60；并将结果写入到磁盘中
    :param buffer: 缓冲区（共8个blk）
    """
    extmem.drop_blk_in_dir(select_linear)  # 删除文件夹下的所有模拟磁盘文件
    two_items, buffer.io_num, count, res = [('r', extmem.blk_num1, 40), ('s', extmem.blk_num2, 60)], 0, 0, []
    for item in two_items:
        for disk_idx in range(item[1]):  # item[1]表示关系占用的物理磁盘块数
            index = buffer.load_blk('%s%s%d.blk' % (disk_dir, item[0], disk_idx))  # 加载磁盘块内容到缓冲区中
            # index指加载到缓冲区中第index块
            for data in buffer.data[index]:
                data0, data1 = data.split()  # 获取2个属性值
                if int(data0) == item[2]:  # 关系选择的条件
                    res.append(data)
                    if len(res) == tuple_num:
                        buffer.write_buffer(res, '%s%s%d.blk' % (select_linear, item[0], count))
                        # count表示存储“关系选择结果”的块的index——r0，r1这种
                        res, count = [], count + 1
            buffer.free_blk(index)  # 这个blk内容已经全部扫描过，故可以释放缓冲区
        if len(res) > 0:
            # 未能填满整个块的result，单独成块
            buffer.write_buffer(res, '%s%s%d.blk' % (select_linear, item[0], count))
            # 此关系已经全部扫描完，准备扫描下一个关系
            res, count = [], 0


def blk_contains_key(buffer: extmem, path: str, target: int) -> int:
    """
    若blk包含target，则返回0，若target大于blk中任何key，则返回1；若target小于blk中任何key，则返回-1
    上述三者都不满足的话，说明target确实在blk的key值区间内，但是blk就是没有一个元组的key和target相等，此时返回-2
    :param buffer: 缓冲区
    :param path: blk的路径
    :param target: 目标key值
    :return: 1 or -1 or 0 or -2
    """
    index = buffer.load_blk(path)
    if target < int(buffer.data[index][0].split()[0]):
        buffer.free_blk(index)
        return -1
    # 遍历这个blk中的元组内容
    for data in buffer.data[index]:
        data0, data1 = data.split()
        if int(data0) == target:
            buffer.free_blk(index)
            return 0
    if target > int(buffer.data[index][-1].split()[0]):
        buffer.free_blk(index)
        return 1
    # 最后一种情况
    buffer.free_blk(index)
    return -2


def find_blk_index(buffer: extmem, relation: str, left: int, right: int, target: int) -> int:
    """
    二分搜索，找到某个blk中的元组的key值包含target， 返回这个blk的index号
    :param buffer: 缓冲区
    :param relation: 表示当前在哪个关系上搜索
    :param left: 左部index
    :param right: 右部index
    :param target: 目标值
    :return: 包含target的blk的index号，若该关系上没有key = target的元组，则返回-1
    """
    if right >= left:
        mid = int(left + (right - left) / 2)
        # 判断mid blk是否包含target
        state = blk_contains_key(buffer, '%s%s%d.blk' % (sort_temp_dir, relation, mid), target)
        if state == 0:
            return mid
        elif state == -2:
            return -1
        elif state == -1:
            # 二分，前往“左半区”搜索
            return find_blk_index(buffer, relation, left, mid - 1, target)
        else:
            # 二分，前往“右半区”搜索
            return find_blk_index(buffer, relation, mid + 1, right, target)
    else:
        return -1


def binary_search(buffer: extmem.Buffer):
    # 先外部排序
    external_sort(buffer)
    # 再在排序结果上进行二分搜索
    extmem.drop_blk_in_dir(select_binary)  # 删除文件夹下的所有模拟磁盘文件
    two_items, buffer.io_num, count, res = [('r', extmem.blk_num1, 40), ('s', extmem.blk_num2, 60)], 0, 0, []
    for item in two_items:
        answer = find_blk_index(buffer, item[0], 0, item[1] - 1, item[2])
        searchOver = False
        if answer == -1:
            continue
        elif answer == 0:
            # 直接向右拓展
            for disk_idx in range(item[1]):  # item[1]表示关系占用的物理磁盘块数
                index = buffer.load_blk('%s%s%d.blk' % (sort_temp_dir, item[0], disk_idx))  # 加载磁盘块内容到缓冲区中
                # index指加载到缓冲区中第index块
                for data in buffer.data[index]:
                    data0, data1 = data.split()  # 获取2个属性值
                    if int(data0) == item[2]:  # 关系选择的条件
                        res.append(data)
                        if len(res) == tuple_num:
                            buffer.write_buffer(res, '%s%s%d.blk' % (select_binary, item[0], count))
                            # count表示存储“关系选择结果”的块的index——r0，r1这种
                            res, count = [], count + 1
                    # target和key不相等，且这种情况发生在非answer号blk上，代表搜索结束
                    elif disk_idx != answer:
                        searchOver = True
                        break
                buffer.free_blk(index)  # 这个blk内容已经全部扫描过，故可以释放缓冲区
                # 搜索结束则直接跳过对后续磁盘上blk的扫描
                if searchOver:
                    break
        elif answer == item[1] - 1:
            start_blk = 0
            # 向左拓展
            for disk_idx in range(answer, 0, -1):
                # 找最终起源blk
                if blk_contains_key(buffer, '%s%s%d.blk' % (sort_temp_dir, item[0], disk_idx), item[2]) == 0 \
                        and blk_contains_key(buffer, '%s%s%d.blk' % (sort_temp_dir, item[0], disk_idx - 1),
                                             item[2]) == 1:
                    start_blk = disk_idx
                    break
            # 从"最终起源blk"开始遍历元组
            for disk_idx in range(start_blk, item[1]):
                index = buffer.load_blk('%s%s%d.blk' % (sort_temp_dir, item[0], disk_idx))  # 加载磁盘块内容到缓冲区中
                # index指加载到缓冲区中第index块
                for data in buffer.data[index]:
                    data0, data1 = data.split()  # 获取2个属性值
                    if int(data0) == item[2]:  # 关系选择的条件
                        res.append(data)
                        if len(res) == tuple_num:
                            buffer.write_buffer(res, '%s%s%d.blk' % (select_binary, item[0], count))
                            # count表示存储“关系选择结果”的块的index——r0，r1这种
                            res, count = [], count + 1
                buffer.free_blk(index)  # 这个blk内容已经全部扫描过，故可以释放缓冲区

        else:
            # 找“最终起源blk”
            start_blk = 0
            # 向左拓展
            for disk_idx in range(answer, 0, -1):
                # 找最终起源blk
                if blk_contains_key(buffer, '%s%s%d.blk' % (sort_temp_dir, item[0], disk_idx), item[2]) == 0 \
                        and blk_contains_key(buffer, '%s%s%d.blk' % (sort_temp_dir, item[0], disk_idx - 1),
                                             item[2]) == 1:
                    start_blk = disk_idx
            # 从“最终起源”开始遍历元组
            for disk_idx in range(start_blk, item[1]):
                index = buffer.load_blk('%s%s%d.blk' % (sort_temp_dir, item[0], disk_idx))  # 加载磁盘块内容到缓冲区中
                # index指加载到缓冲区中第index块
                for data in buffer.data[index]:
                    data0, data1 = data.split()  # 获取2个属性值
                    if int(data0) == item[2]:  # 关系选择的条件
                        res.append(data)
                        if len(res) == tuple_num:
                            buffer.write_buffer(res, '%s%s%d.blk' % (select_binary, item[0], count))
                            # count表示存储“关系选择结果”的块的index——r0，r1这种
                            res, count = [], count + 1
                    # 当在answer右边遇到不匹配时，则无需继续扫描
                    elif disk_idx > answer:
                        searchOver = True
                        break
                buffer.free_blk(index)  # 这个blk内容已经全部扫描过，故可以释放缓冲区
                if searchOver:
                    break

        if len(res) > 0:
            # 未能填满整个块的result，单独成块
            buffer.write_buffer(res, '%s%s%d.blk' % (select_binary, item[0], count))
            # 此关系已经全部扫描完，准备扫描下一个关系
            res, count = [], 0
    buffer.io_num = 20

class InnerNode:
    """
    会物化到文件中，命名为height_index.node
    """
    def __init__(self, height = 0, index = 0):
        self.height = height
        self.index = index
        self.keys = [-1]*7
        # pointers存储的也是儿子节点的index，儿子节点的height就是self.height-1
        self.pointers = [-1]*8
        # father用来保存父亲节点的index属性值，因为height值+1就是父亲的height
        self.father_index = -1

    def __str__(self):
        res = str(self.keys)+"\n"+str(self.pointers)
        return res

    def haveFreeSpace(self):
        """
        查看本节点是否有剩余空间
        :return: True if have free space，False if not
        """
        return self.keys[-1] == -1

    def findFirstFreeKeyIndex(self):
        """
        寻找本节点第一个“现成”的可用key空间的index
        :return: 第一个可用key空间的index，若没有，则返回-1
        """
        for i in range(0, len(self.keys)):
            if self.keys[i] == -1:
                return i
        return -1

    def findFather(self, all_node: list):
        """
        在all_node中寻找父亲节点
        :param all_node: 全部节点的列表
        :return: 满足条件（height与index都符合）的节点，若没有，则返回None
        """
        father_height = self.height+1
        father_index = self.father_index
        for node in all_node:
            if node.height == father_height and node.index == father_index:
                return node
        return None

    def findRightMostChild(self, all_node: list):
        """
        从all_node中寻找满足条件的“最右边”的儿子
        :param all_node: 全部节点的列表
        :return: 若存在，则返回node，否则返回None
        """
        child_index = -1
        child_height = self.height - 1
        for i in range(len(self.pointers)-1, -1, -1):
            if self.pointers[i] != -1:
                child_index = self.pointers[i]
                break
        for node in all_node:
            if node.height == child_height and node.index == child_index:
                return node
        return None


def findNode(all_node: list, height: int, index: int):
    for node in all_node:
        if node.height == height and node.index == index:
            return node
    return None


def updateNode(all_node: list, newNode: InnerNode):
    """
    更新node，使用新节点来替换旧节点
    :param all_node: 节点列表
    :param newNode: 新节点
    """
    for node in all_node:
        if node.height == newNode.height and node.index == newNode.index:
            node = newNode
            break


def construct_tree(buffer: extmem.Buffer):
    extmem.drop_blk_in_dir(BPlusTree_node)  # 删除文件夹下的所有模拟磁盘文件
    two_items, buffer.io_num, root_path = [('r', extmem.blk_num1, 40), ('s', extmem.blk_num2, 60)], 0, []
    for item in two_items:
        # 全部节点
        all_node = []
        # 初始节点
        startNode = InnerNode()
        all_node.append(startNode)
        # 当前树中“最右边”的有空闲位置的节点
        freeNode = None
        # 高度和index映射表
        height_index = {}

        for disk_idx in range(item[1]):  # item[1]表示关系占用的物理磁盘块数
            path = '%s%s%d.blk' % (sort_temp_dir, item[0], disk_idx)
            index = buffer.load_blk(path)  # 加载磁盘块内容到缓冲区中
            # 获取newKey
            newKey = int(buffer.data[index][0].split()[0])
            buffer.free_blk(index)
            # 找整个树系统中“最右边”的有空闲位置的节点
            # 首先找到最顶层的节点,并在此过程中寻找freeNode
            topNode = startNode
            freeNode = None
            if topNode.haveFreeSpace():
                freeNode = topNode

            while topNode.findFather(all_node) is not None:
                topNode = topNode.findFather(all_node)

            # 记录顶层高度
            max_height = topNode.height

            if topNode.height != 0:
                # 从顶层节点开始下降，一直找“最右边”的儿子节点
                while topNode.findRightMostChild(all_node).height != 0:
                    # 将height——index对进行保存
                    height_index[topNode.height] = topNode.index
                    # 首先更新freeNode
                    if topNode.haveFreeSpace():
                        freeNode = topNode
                    topNode = topNode.findRightMostChild(all_node)

                # 首先更新freeNode
                if topNode.haveFreeSpace():
                    freeNode = topNode
                topNode = topNode.findRightMostChild(all_node)

            # 现在的topNode就是树系统中“最右边”的节点，再度更新freenode
            height_index[0] = topNode.index
            if topNode.haveFreeSpace():
                freeNode = topNode

            # 整个树系统没有空间了
            if freeNode is None:
                # 处理新的顶层节点
                newTopNode = InnerNode(max_height+1, 0)
                newNode = InnerNode(height=max_height, index=height_index[max_height] + 1)
                all_node.append(newTopNode)
                all_node.append(newNode)
                newTopNode.keys[0] = newKey
                newTopNode.pointers[0] = height_index[max_height]
                newTopNode.pointers[1] = newTopNode.pointers[0]+1
                height_index[max_height+1] = 0
                # 添加父亲联系
                topNode.father_index = 0
                newNode.father_index = 0
                for height in range(max_height, 0, -1):
                    newNode = InnerNode(height=height - 1, index=height_index[height] + 1)
                    all_node.append(newNode)
                    # 找到这一层中新创建的那个节点
                    node = findNode(all_node, height, height_index[height] + 1)
                    node.pointers[0] = newNode.index
                # 将高度为0的节点连接到path
                node = findNode(all_node, 0, height_index[0] + 1)
                node.pointers[0] = path

            # 有“现成”的空间可用
            elif freeNode.height == 0:
                # 找到第一个可用的“现成”空间
                firstFreeKeyIndex = freeNode.findFirstFreeKeyIndex()
                freeNode.keys[firstFreeKeyIndex] = newKey
                # 高为0的节点直接指向path即可
                freeNode.pointers[firstFreeKeyIndex+1] = path
            # 可以“挤出”空间使用
            else:
                firstFreeKeyIndex = freeNode.findFirstFreeKeyIndex()
                # 创建新节点
                for height in range(freeNode.height, 0, -1):
                    newNode = InnerNode(height=height-1, index=height_index[height-1] + 1)
                    all_node.append(newNode)
                    if height == freeNode.height:
                        freeNode.keys[firstFreeKeyIndex] = newKey
                        freeNode.pointers[firstFreeKeyIndex + 1] = newNode.index
                    else:
                        # 找到这一层中新创建的那个节点
                        node = findNode(all_node, height, height_index[height] + 1)
                        node.pointers[0] = newNode.index
                # 将高度为0的节点连接到path
                node = findNode(all_node, 0, height_index[0] + 1)
                node.pointers[0] = path

        # 将all_node结果物化
        root_height = 0
        for node in all_node:
            save_path = '%s%s_%d_%d.node' % (BPlusTree_node, item[0], node.height, node.index)
            with open(save_path, 'w') as f:
                f.write(str(node))
            if node.height > root_height:
                root_height = node.height
        root_path.append('%s%s_%d_%d.node' % (BPlusTree_node, item[0], root_height, 0))
    return root_path


def str2list(inputStr: str):
    """
    将'[1, 2, 3]'形式的字符串转变为数组[1, 2, 3]
    :param inputStr: 输入的字符串
    :return: 对应的int数组
    """
    pattern = re.compile(r'\d+')
    x = []
    res = re.findall(pattern, inputStr)
    for i in res:
        x.append(int(i))
    return x


def str2pathList(inputStr: str):
    x = inputStr.split(', ')
    x[0] = x[0][1:]
    x[-1] = x[-1][:-1]
    return x


def BPlusTree_search(buffer: extmem.Buffer):
    # 先建立树
    root_path = construct_tree(buffer)
    extmem.drop_blk_in_dir(BPlusTree_result)  # 删除文件夹下的所有模拟磁盘文件
    two_items, buffer.io_num, count, res = [('r', extmem.blk_num1, 40), ('s', extmem.blk_num2, 60)], 0, 0, []
    for item in two_items:
        # 加载根节点到内存中,存储形式为[[key0,key1...key6],[ptr0, ptr1...ptr7]]
        path = root_path[0] if item[0] == 'r' else root_path[1]
        index = buffer.load_blk(path)
        keyList = str2list(buffer.data[index][0])
        ptrList = str2list(buffer.data[index][1])
        # 获取该节点第一个key=-1出现的index
        rightMostIndex = -1
        for i in range(tuple_num):
            if keyList[i] == -1:
                rightMostIndex = i
        # 得到最后一个不是-1的key的所在index
        if rightMostIndex != -1:
            rightMostIndex -= 1
        else:
            rightMostIndex = tuple_num-1

        # 找到最底层
        while int(path.split('_')[1]) != 0:
            # 依据target选择更新path
            if item[2] <= keyList[0]:
                # 更新为ptr0指向的blk
                path = '%s%s_%d_%d.node' % (BPlusTree_node, item[0], int(path.split('_')[1])-1,
                                            ptrList[0])
            elif item[2] > keyList[rightMostIndex]:
                path = '%s%s_%d_%d.node' % (BPlusTree_node, item[0], int(path.split('_')[1])-1,
                                            ptrList[rightMostIndex+1])
            else:
                for i in range(rightMostIndex):
                    if keyList[i] <= item[2] <= keyList[i+1]:
                        path = '%s%s_%d_%d.node' % (BPlusTree_node, item[0], int(path.split('_')[1])-1,
                                                    ptrList[i+1])
            buffer.free_blk(0)
            index = buffer.load_blk(path)
            keyList = str2list(buffer.data[index][0])
            ptrList = str2list(buffer.data[index][1])
            # 获取该节点第一个key=-1出现的index
            rightMostIndex = -1
            for i in range(tuple_num):
                if keyList[i] == -1:
                    rightMostIndex = i
            # 得到最后一个不是-1的key的所在index
            if rightMostIndex != -1:
                rightMostIndex -= 1
            else:
                rightMostIndex = tuple_num - 1

        # 现在内存中加载了height = 0的节点，在buffer.data[index]处
        pathList = str2pathList(buffer.data[index][1])
        # 依据target选择更新path
        if item[2] <= keyList[0]:
            # TODO
            path = pathList[0]
        elif item[2] > keyList[rightMostIndex]:
            path = pathList[rightMostIndex+1]
        else:
            for i in range(rightMostIndex):
                if keyList[i] <= item[2] <= keyList[i+1]:
                     path = pathList[i+1]
        # 读取指定blk并向后拓展
        for disk_idx in range(str2list(path)[0], item[1]):  # item[1]表示关系占用的物理磁盘块数
            index = buffer.load_blk('%s%s%d.blk' % (sort_temp_dir, item[0], disk_idx))  # 加载磁盘块内容到缓冲区中
            # index指加载到缓冲区中第index块
            for data in buffer.data[index]:
                data0, data1 = data.split()  # 获取2个属性值
                if int(data0) == item[2]:  # 关系选择的条件
                    res.append(data)
                    if len(res) == tuple_num:
                        buffer.write_buffer(res, '%s%s%d.blk' % (BPlusTree_result, item[0], count))
                        # count表示存储“关系选择结果”的块的index——r0，r1这种
                        res, count = [], count + 1
            buffer.free_blk(index)  # 这个blk内容已经全部扫描过，故可以释放缓冲区
        if len(res) > 0:
            # 未能填满整个块的result，单独成块
            buffer.write_buffer(res, '%s%s%d.blk' % (BPlusTree_result, item[0], count))
            # 此关系已经全部扫描完，准备扫描下一个关系
            res, count = [], 0
