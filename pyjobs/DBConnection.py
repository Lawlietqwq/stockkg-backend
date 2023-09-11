#----打开数据库-----
from py2neo import Node, Relationship, Graph, NodeMatcher, RelationshipMatcher,Subgraph
graph = Graph("", auth=("neo4j", "csubigdata"))

