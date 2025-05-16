from elasticsearch import Elasticsearch

# Подключение к Elasticsearch
es = Elasticsearch([{'host': 'localhost', 'port': 9200}])

def index_log(doc: dict, index_name: str = 'logs'):
    """Индексация логов в Elasticsearch."""
    es.index(index=index_name, document=doc)

def search_logs(query: str, limit: int = 10):
    """Поиск логов в Elasticsearch."""
    body = {
        "_source": ["timestamp", "level", "message"],
        "query": {
            "match": {
                "message": query
            }
        },
        "size": limit
    }
    res = es.search(index="logs", body=body)
    return [{"timestamp": hit["_source"]["timestamp"],
             "level": hit["_source"]["level"],
             "message": hit["_source"]["message"]}
            for hit in res['hits']['hits']]
