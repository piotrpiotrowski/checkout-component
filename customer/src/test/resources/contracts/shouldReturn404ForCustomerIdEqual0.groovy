import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url "/customers/" + 0
    }
    response {
        status 404
        body("""
  {
    "message": "Not found customer with id 0",
    "code": "NOT_FOUND",
    "status": 404,
    "details":[
        {
         "code": "not-exist",
         "field": "customerId",
         "value":0
        }
    ]
  }
  """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
}