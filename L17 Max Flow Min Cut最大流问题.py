from collections import deque
'''
算法示意，最为便捷地利用邻接矩阵表示边的方法
'''
def bfs(residual_graph,source,sink,parent):
    visited=set()
    queue=deque()
    queue.append(source)
    visited.add(source)
    while queue:
        current=queue.popleft()
        for v,capacity in enumerate(residual_graph[current]):
            if v not in visited and capacity>0:
                queue.append(v)
                visited.add(v)
                parent[v]=current
                if v==sink:
                    return True
    return False
def edmonds_karp(graph,source,sink):
    residual_graph=[row for row in graph]
    parent=[-1]*len(graph)
    max_flow=0
    while bfs(residual_graph,source,sink,parent):
        path_flow=float('Inf')
        s=sink
        while s!=source:
            path_flow=min(path_flow,residual_graph[parent[s]][s])#确定利用bfs找到的路径上每一段流量的最小值作为总流量计入
            s=parent[s]
        max_flow+=path_flow
        v=sink
        while v!=source:#把已计入的流量从路径上分别减去，并设置正向剩余边的值和反向可撤回边的值
            u=parent[v]
            residual_graph[u][v]-=path_flow
            residual_graph[v][u]+=path_flow
            v=parent[v]
    return max_flow
if __name__ == "__main__":
    graph = [
        [0, 16, 13, 0, 0, 0],
        [0, 0, 10, 12, 0, 0],
        [0, 4, 0, 0, 14, 0],
        [0, 0, 9, 0, 0, 20],
        [0, 0, 0, 7, 0, 4],
        [0, 0, 0, 0, 0, 0]
    ]
    source = 0
    sink = 5
    print("最大流量为:", edmonds_karp(graph, source, sink))
'''
ProblemSet7中的题目：维护一个边容量动态变化的图中的最大流路径
思路：对于某条边容量升高，则只需要在当前residual network中修改对应的正向边即可
对于边uv容量减小至r：
如果总流量小于r，则无任何变化
否则，每次按如下操作：
将边uv上流量减小1，为了符合流量守恒，从u开始，在residual network中沿反向边深搜找到源点s，汇点t或v
然后将路径上的流量-1
如果找到的是v，则v的流出流量也-1，不用修改了
如果不是，则从v开始在residual network中沿正向边深搜找到t或s，然后将路径上的流量-1即可
时间复杂度2*O(k(V+E))(采用DFS)
'''