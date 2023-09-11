import tushare as ts
import time
import pandas as pd
import numpy as np
import datetime
from apscheduler.schedulers.blocking import BlockingScheduler
from py2neo import Node, Relationship, Graph, NodeMatcher, RelationshipMatcher,Subgraph
import redis
TUSHARE_TOKEN = ''
def update_db():
    ts.set_token(TUSHARE_TOKEN)
    pro = ts.pro_api()
    basic_data = pro.stock_basic(exchange='', list_status='L', fields='ts_code,symbol,name,industry')
    redis_conn = redis.Redis(host='www.csubigdata.com', port=6397)
    # redis_conn = redis.Redis(host='121.89.234.19', port=6397)
    for i in range(len(basic_data)):
        record = basic_data.iloc[i]
        industry = record['industry']
        if industry == None: continue
        ts_code = record['ts_code']
        name = record['name']
        print(ts_code)
        redis_conn.hset(industry, ts_code, name)

def update_codes():
    ts.set_token(TUSHARE_TOKEN)
    pro = ts.pro_api()
    basic_data = pro.stock_basic(exchange='', list_status='L', fields='ts_code,symbol,name,industry')
    redis_conn = redis.Redis(host='www.csubigdata.com', port=6397)
    # redis_conn = redis.Redis(host='121.89.234.19', port=6397)
    for i in range(len(basic_data)):
        record = basic_data.iloc[i]
        industry = record['industry']
        if industry == None: continue
        ts_code = record['ts_code']
        print(ts_code)
        redis_conn.sadd("codes", ts_code)

def update_industry():
    redis_conn = redis.Redis(host='www.csubigdata.com', port=6397)
    list = redis_conn.keys("*")
    for i in list:
        x = i.decode()
        redis_conn.sadd("industry", x)


def match_root_node(graph, m_label, ts_code, ann_date=None):
    # m_label 为 Stock
    matcher = NodeMatcher(graph)
    if ann_date is None:
        node = matcher.match(m_label, ts_code=ts_code).first()
    else:
        node = matcher.match(m_label, ts_code=ts_code, ann_date=ann_date).first()
    return node

def match_child_node(graph, m_label, name):
    # m_label 为 Holder
    matcher = NodeMatcher(graph)
    node = matcher.match(m_label, name=name).first()
    return node

def match_relationship(graph,holder, ann_date, ts_code):
    # m_label 为 Holder
    matcher = RelationshipMatcher(graph)
    relationship = matcher.match((holder, None), ann_date=ann_date, ts_code=ts_code).first()
    return relationship

def match_last_relationship(graph,holder_name, ann_date, ts_code):
    cypher_ = "MATCH (h:Holder)-[r:持有]->(s:Stock) WHERE h.name='{}' AND r.ann_date<{} AND r.ts_code='{}' RETURN r.ann_date ORDER BY r.ann_date LIMIT 1".format(holder_name,ann_date,ts_code)
    expires = graph.run(cypher_)
    return pd.DataFrame(expires)

def update_relationship(graph, holder_name, ann_date, ts_code):
    cypher_ = "MATCH (h:Holder)-[r:持有]->(s:Stock) WHERE h.name='{}' AND r.ann_date<{} AND r.ts_code='{}' SET r.expires={} RETURN r.expires ORDER BY r.ann_date LIMIT 1".format(holder_name,ann_date,ts_code,ann_date)
    expires = graph.run(cypher_)
    return pd.DataFrame(expires)

def create_relationship(graph):
    pass

def delete_all_data(graph):
    graph.delete_all()


def get_data():

    pro = ts.pro_api()
    now = datetime.datetime.now()
    end_date = now.strftime("%Y%m%d")
    basic_data = pro.stock_basic(exchange='', list_status='L', fields='ts_code,symbol,name,industry')
    code_list = basic_data['ts_code']
    graph = Graph("neo4j://csubigdata.com:7687", auth=("neo4j", "csubigdata"))
    last_modified = match_last_relationship(graph, "中信证券-中信银行-中信证券卓越成长两年持有期混合型集合资产管理计划", 20230808, "000001.SZ")
    for idx in range(len(basic_data)):
        single_basic = basic_data.iloc[idx]
        code = single_basic['ts_code']
        # df = pro.top10_holders(ts_code=code, start_date=str(int(end_date)-20000), end_date=end_date).iloc[0:9]
        # if len(df) == 0: continue
        df_overall = pro.top10_holders(ts_code=code, start_date=str(int(end_date)-20000), end_date=end_date)
        group_num = int(len(df_overall)/10)
        if len(df_overall) == 0:
            continue
        for df_idx in range(group_num):
            df = df_overall.iloc[-df_idx*10-1:-df_idx*10-10:-1]
            ann_date = df['ann_date'].iloc[0]
            ann_date = int(ann_date)
            search_node = match_root_node(graph, 'Stock', code, ann_date)
            if search_node is not None: continue
            node_root = Node("Stock", name=single_basic['name'], ts_code=code, industry=single_basic['industry'], ann_date=ann_date)
            graph.create(node_root)
            for h_idx in range(len(df)):
                holder = df.iloc[h_idx]
                holder_name = holder['holder_name']
                ts_code = holder['ts_code']
                search_node = match_child_node(graph, 'Holder', holder_name)
                if search_node is None:
                    search_node = Node("Holder", name=holder_name)
                    graph.create(search_node)
                else:
                    update_relationship(graph, holder_name, ann_date, ts_code)
                relation = Relationship(search_node, '持有', node_root, ts_code=ts_code, holder_name=holder_name, hold_amount=str(holder['hold_amount']), hold_ratio=str(holder['hold_ratio']), ann_date=ann_date, expires=99999999)
                print(holder_name, '=====>', single_basic['name'], '(', code, ')')
                graph.create(relation)


# delete_all_data(Graph("neo4j://csubigdata.com:7687", auth=("neo4j", "csubigdata")))
# get_data()
update_codes()
# sched = BlockingScheduler()
# sched.add_job(get_data, 'cron', day_of_week='mon-fri', hour=5, minute=30)
