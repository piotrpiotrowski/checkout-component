import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/customers/0/orders"
        body("""
          {
          "items": [
            {
              "id": "2",
              "quantity": 1
            }
          ]
        }
        """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
    response {
        status 404
        body("""
        {
        "message": "Not found customer with id 0",
        "code": "NOT_FOUND",
        "details":[
            {
             "code": "not-exist",
             "field": "customerId",
             "value": 0
            }
        ]
       }
        """)
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}