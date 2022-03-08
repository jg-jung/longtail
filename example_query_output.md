{
    "took": 2,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 1,
            "relation": "eq"
        },
        "max_score": null,
        "hits": [
            {
                "_index": "orders",
                "_type": "_doc",
                "_id": "6924066a-604b-4366-a42b-f5d8f970ce0a",
                "_score": null,
                "_source": {
                    "charge": {
                        "chargeHistId": "5a6118a1-7afa-4f12-b263-893dccee16e3",
                        "orderId": "6924066a-604b-4366-a42b-f5d8f970ce0a"
                    },
                    "freight": {
                        "unloadingCompletedDateTime": 10,
                        "loadingCompletedDateTime": 1645666669594
                    }
                },
                "sort": [
                    1745666633842
                ]
            }
        ]
    }
}