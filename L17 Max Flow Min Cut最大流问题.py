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