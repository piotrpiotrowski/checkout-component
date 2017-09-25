import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/items/discounts"
        body([
                "id"      : $(regex('([5-9]|[1-9][0-9][0-9]*)')),
                "quantity": $(regex('[0-9]*'))
        ])
        headers {
            header('Content-Type': 'application/json')
        }
    }
    response {
        status 404
        body("""
  {
    "message": "Not all items found",
    "code": "NOT_FOUND",
    "status": 404,
    "details":[
        {
         "code": "not-exist",
         "field": "ids",
         "value": [32]
        }
    ]
  }
  """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
}