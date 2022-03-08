/*
path : localhost:{post}/index/_search 
*/

{
  "from": 0, //paging 
  "size": 10,
  "query": {    
    "bool": {
      "must": [
        {
            "match_phrase": 
            { 
                "freightOwnerType": "contractFreightOwner" 
            }
        },
        {
            "match_phrase":
            { 
                "charge.orderId": "6924066a-604b-4366-a42b-f5d8f970ce0a" 
            }
        },
        {
            "range": {
              "freight.loadingCompletedDateTime": {
                "gte": 1645666669593,
                "lte": 1645666669595
                // "gte":"01/09/2017",
                //"lte":"30/09/2017",
                //"format": "dd/MM/yyyy"
              }
            }
          },
          {
            "range": {
              "freight.unloadingCompletedDateTime": {
                "gte": 0,
                "lte": 20
                // "gte":"01/09/2017",
                //"lte":"30/09/2017",
                //"format": "dd/MM/yyyy"
              }
            }
          }
      ]

    }
  },
  //Specify return fields
  "_source": ["charge.chargeHistId", "charge.orderId", "freight.loadingCompletedDateTime", "freight.unloadingCompletedDateTime"],
//"_source": {"includes": ["charge.chargeHistId", "charge.orderId", "freight.loadingCompletedDateTime", "freight.unloadingCompletedDateTime"]},
  "sort": [
    { "loadingCompletedDateTime": "asc" } //order by asc, des
  ]
}

/*
    filter를 AND, OR, NOT 연산으로 결합
    must(AND) / should(OR) / must_not(NOT)

    "range": {
        "startTime": {
          "from": "2015-10-20T13:00-04:00",
          "to": "2015-10-20T14:00-04:00"
        }
      }
*/